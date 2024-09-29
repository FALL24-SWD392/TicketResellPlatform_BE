package com.swd392.ticket_resell_be.services.impls;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.*;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.LoginDtoResponse;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.BlacklistTokenService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.*;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImplement implements UserService {

    UserRepository userRepository;
    BlacklistTokenService blacklistTokenService;
    PasswordEncoder passwordEncoder;
    ApiResponseBuilder apiResponseBuilder;
    GoogleTokenUtil googleTokenUtil;
    TokenUtil tokenUtil;
    EmailUtil emailUtil;
    PagingUtil pagingUtil;

    @Override
    public ApiItemResponse<LoginDtoResponse> login(LoginDtoRequest loginDtoRequest) throws JOSEException {
        User user = userRepository.findByUsername(loginDtoRequest.username())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(loginDtoRequest.password(), user.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        LoginDtoResponse loginDtoResponse = new LoginDtoResponse(tokenUtil.generateAccessToken(user),
                tokenUtil.generateRefreshToken(user));
        return apiResponseBuilder.buildResponse(loginDtoResponse, HttpStatus.OK);
    }

    @Override
    public ApiItemResponse<LoginDtoResponse> login(String token)
            throws JOSEException {
        String email = googleTokenUtil.getEmail(token);
        User user = userRepository.findByEmailAndTypeRegister(email, Categorize.GOOGLE)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        LoginDtoResponse loginDtoResponse = new LoginDtoResponse(tokenUtil.generateAccessToken(user),
                tokenUtil.generateRefreshToken(user));
        return apiResponseBuilder.buildResponse(loginDtoResponse, HttpStatus.OK);
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
        User user = new User();
        user.setUsername(registerDtoRequest.username());
        user.setEmail(registerDtoRequest.email());
        user.setPassword(passwordEncoder.encode(registerDtoRequest.password()));
        user.setStatus(Categorize.UNVERIFIED);
        user.setRole(Categorize.MEMBER);
        user.setTypeRegister(Categorize.SYSTEM);
        userRepository.save(user);
        emailUtil.sendVerifyEmail(user.getEmail(), user.getUsername(), tokenUtil.generateAccessToken(user));
        return apiResponseBuilder.buildResponse(HttpStatus.CREATED, "Please verify your email!");
    }

    @Override
    public ApiItemResponse<String> register(RegisterGoogleDtoRequest request) {
        String email = googleTokenUtil.getEmail(request.token());
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setEmail(email);
        user.setStatus(Categorize.VERIFIED);
        user.setRole(Categorize.MEMBER);
        user.setTypeRegister(Categorize.GOOGLE);
        userRepository.save(user);
        return apiResponseBuilder.buildResponse(HttpStatus.CREATED, "Create account successfully!");
    }

    @Override
    public ApiItemResponse<String> verifyEmail(String token) {
        User user = userRepository.findByUsername(tokenUtil.getUsernameFromToken(token))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(Categorize.VERIFIED);
        userRepository.save(user);
        blacklistTokenService.save(tokenUtil.generateBlacklistToken(token));
        return apiResponseBuilder.buildResponse("Email verified!", HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<String> logout(String token) {
        blacklistTokenService.save(tokenUtil.generateBlacklistToken(token));
        return apiResponseBuilder.buildResponse("Logout successfully!", HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<String> refreshToken(String token) throws JOSEException {
        User user = new User();
        user.setUsername(tokenUtil.getUsernameFromToken(token));
        return apiResponseBuilder.buildResponse(tokenUtil.generateAccessToken(user),
                HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<String> changePassword(ChangePasswordDtoRequest changePasswordDtoRequest) {
        if (!changePasswordDtoRequest.newPassword().equals(changePasswordDtoRequest.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(changePasswordDtoRequest.oldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(changePasswordDtoRequest.newPassword()));
        userRepository.save(user);
        return apiResponseBuilder.buildResponse("Password changed successfully!",
                HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<String> forgotPassword(String email) throws MessagingException, JOSEException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        emailUtil.sendResetPassword(user.getEmail(), user.getUsername(),
                tokenUtil.generateAccessToken(user));
        return apiResponseBuilder.buildResponse("Please check your email to reset password!",
                HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<String> resetPassword(String token, ResetPasswordDtoRequest resetPasswordDtoRequest)
            throws JOSEException {
        if (!resetPasswordDtoRequest.newPassword().equals(resetPasswordDtoRequest.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(resetPasswordDtoRequest.newPassword()));
        userRepository.save(user);
        blacklistTokenService.save(tokenUtil.generateBlacklistToken(token));
        return apiResponseBuilder.buildResponse("Password reset successfully!",
                HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<User> getUserByUsername(String username) {
        return apiResponseBuilder
                .buildResponse(userRepository.findByUsername(username)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)),
                        HttpStatus.OK, null);
    }

    @Override
    public ApiListResponse<User> getUsers(PageDtoRequest pageDtoRequest) {
        Page<User> users = userRepository.findAll(pagingUtil.getPageable(pageDtoRequest));
        return apiResponseBuilder.buildResponse(users.getContent(), users.getSize(), users.getNumber(),
                users.getTotalElements(), users.getTotalPages(), HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<String> deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(Categorize.REMOVED);
        userRepository.save(user);
        return apiResponseBuilder.buildResponse("User deleted successfully!", HttpStatus.OK, null);
    }

    @Override
    public ApiItemResponse<User> createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return apiResponseBuilder.buildResponse(user, HttpStatus.CREATED, null);
    }

    @Override
    public ApiItemResponse<Object> getCurrentUser() {
        Object member = SecurityContextHolder.getContext().getAuthentication().getName();
        return apiResponseBuilder.buildResponse(member, HttpStatus.OK, null);
    }


    @Override
    public Optional<User> getUserByName(String username) throws AppException {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                return user;
            } else {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
    }


}
