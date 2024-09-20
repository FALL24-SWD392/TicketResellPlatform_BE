package com.swd392.ticket_resell_be.services.impls;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.RegisterDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.LoginDtoResponse;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.EmailUtil;
import com.swd392.ticket_resell_be.utils.TokenUtil;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImplement implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ApiResponseBuilder apiResponseBuilder;
    TokenUtil tokenUtil;
    EmailUtil emailUtil;

    @Override
    public ApiItemResponse<LoginDtoResponse> login(LoginDtoRequest loginDtoRequest) throws JOSEException {
        User user = userRepository.findByUsername(loginDtoRequest.username())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(loginDtoRequest.password(), user.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        LoginDtoResponse loginDtoResponse = new LoginDtoResponse(tokenUtil.generateAccessToken(user),
                tokenUtil.generateRefreshToken(user));
        return apiResponseBuilder.buildResponse(loginDtoResponse, HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<String> register(RegisterDtoRequest registerDtoRequest)
            throws JOSEException, MessagingException {
        if (!registerDtoRequest.password().equals(registerDtoRequest.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        if (userRepository.existsByUsername(registerDtoRequest.username())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(registerDtoRequest.email())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = User.builder()
                .username(registerDtoRequest.username())
                .password(passwordEncoder.encode(registerDtoRequest.password()))
                .email(registerDtoRequest.email())
                .status(Categorize.INACTIVE)
                .role(Categorize.MEMBER)
                .typeRegister(Categorize.SYSTEM)
                .build();
        userRepository.save(user);
        emailUtil.sendVerifyEmail(user.getEmail(), user.getUsername(), tokenUtil.generateAccessToken(user));
        return apiResponseBuilder.buildResponse("Please verify your email!",
                HttpStatus.CREATED, null);
    }

    @Override
    public ApiItemResponse<Object> getCurrentUser() {
        Object member = SecurityContextHolder.getContext().getAuthentication().getName();
        return apiResponseBuilder.buildResponse(member, HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<String> verifyEmail(String token) {
        User user = userRepository.findByUsername(tokenUtil.getUsernameFromToken(token))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(Categorize.ACTIVE);
        userRepository.save(user);
        return apiResponseBuilder.buildResponse("Email verified!", HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<User> getUserByUsername(String username) {
        return apiResponseBuilder
                .buildResponse(userRepository.findByUsername(username)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)),
                        HttpStatus.OK, null);
    }

}
