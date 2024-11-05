package com.swd392.ticket_resell_be.services;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.*;
import com.swd392.ticket_resell_be.dtos.responses.*;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.exceptions.AppException;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    ApiItemResponse<LoginDtoResponse> login(LoginDtoRequest loginDtoRequest) throws JOSEException;

    ApiItemResponse<LoginDtoResponse> login(String token) throws JOSEException;

    ApiItemResponse<String> register(RegisterDtoRequest registerDtoRequest)
            throws JOSEException, MessagingException;

    ApiItemResponse<String> register(RegisterGoogleDtoRequest request);

    ApiItemResponse<String> verifyEmail(String token);

    ApiItemResponse<String> logout(String token);

    ApiItemResponse<LoginDtoResponse> getAccessToken(String token) throws JOSEException;

    ApiItemResponse<String> changePassword(ChangePasswordDtoRequest changePasswordDtoRequest);

    ApiItemResponse<String> forgotPassword(String email) throws MessagingException, JOSEException;

    ApiItemResponse<String> resetPassword(ResetPasswordDtoRequest resetPasswordDtoRequest)
            throws JOSEException;

    ApiItemResponse<UserDto> getCurrentUser();

    ApiListResponse<UserDto> getUsers(String search, int page, int size, Sort.Direction direction, String... properties);

    ApiItemResponse<UserDto> getUserByUsername(String username);

    ApiItemResponse<String> deleteUser(String username);

    ApiItemResponse<User> createUser(User user);

    Optional<User> getUserByName(String username) throws AppException;

    boolean updateReputation(int reputation, String username);

    boolean updateRating(float rating, String username);

    ApiItemResponse<UserDto> updateAvatar(String avatar);

    ApiItemResponse<User> updateUser(String username, User user);

    ApiItemResponse<String> banUser(String username);

    void saveUser(User user);

    void disconnect(User user);

    ApiListResponse<UserDtoWebSocket> findConnectedUsers(int page, int size, Sort.Direction direction, String... properties);

    User findById(UUID id);
}
