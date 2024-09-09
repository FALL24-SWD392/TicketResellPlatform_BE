package com.swd392.ticket_resell_be.services.impls;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.TokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImplement implements UserService {
    @NonFinal
    @Value("${JWT_SECRET_KEY}")
    String secretKey;

    UserRepository userRepository;

    @Override
    public ApiItemResponse<?> login(LoginDtoRequest loginDtoRequest) throws JOSEException {
        User user = userRepository.findByUsername(loginDtoRequest.username())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!user.getPassword().equals(loginDtoRequest.password()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        return ApiResponseBuilder.buildResponse(TokenUtil.generateToken(user, secretKey), HttpStatus.OK);
    }
}
