package com.swd392.ticket_resell_be.services;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.*;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.LoginDtoResponse;
import com.swd392.ticket_resell_be.entities.User;
import jakarta.mail.MessagingException;

public interface UserService {
    ApiItemResponse<LoginDtoResponse> login(LoginDtoRequest loginDtoRequest) throws JOSEException;

    ApiItemResponse<String> register(RegisterDtoRequest registerDtoRequest)
            throws JOSEException, MessagingException;

    ApiItemResponse<String> verifyEmail(String token);

    ApiItemResponse<String> logout(String token);

    ApiItemResponse<String> refreshToken(String token) throws JOSEException;

    ApiItemResponse<String> changePassword(ChangePasswordDtoRequest changePasswordDtoRequest);

    ApiItemResponse<String> forgotPassword(String email) throws MessagingException, JOSEException;

    ApiItemResponse<String> resetPassword(String token, ResetPasswordDtoRequest resetPasswordDtoRequest)
            throws JOSEException;

    ApiItemResponse<Object> getCurrentUser();

    ApiItemResponse<User> getUserByUsername(String username);

    ApiListResponse<User> getUsers(PageDtoRequest pageDtoRequest);

    ApiItemResponse<String> deleteUser(String username);

    ApiItemResponse<User> createUser(User user);

}
