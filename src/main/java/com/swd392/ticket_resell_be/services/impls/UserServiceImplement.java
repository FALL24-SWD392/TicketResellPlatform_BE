package com.swd392.ticket_resell_be.services.impls;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.*;
import com.swd392.ticket_resell_be.dtos.responses.*;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.TokenService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.*;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImplement implements UserService {

    UserRepository userRepository;
    TokenService tokenService;
    PasswordEncoder passwordEncoder;
    ApiResponseBuilder apiResponseBuilder;
    GoogleTokenUtil googleTokenUtil;
    TokenUtil tokenUtil;
    EmailUtil emailUtil;
    PagingUtil pagingUtil;

    @Override
    public ApiItemResponse<LoginDtoResponse> login(LoginDtoRequest loginDtoRequest) throws JOSEException {
        User user = userRepository.findByUsernameAndStatus(loginDtoRequest.username(), Categorize.VERIFIED)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(loginDtoRequest.password(), user.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        String refreshToken = tokenUtil.generateRefreshToken(user);
        //Save refresh-token to db
        tokenService.save(tokenUtil.createTokenEntity(refreshToken, Categorize.ACTIVE));
        //return access-token and refresh-token
        return apiResponseBuilder.buildResponse(new LoginDtoResponse(tokenUtil.generateAccessToken(user), refreshToken),
                HttpStatus.OK);
    }

    @Override
    public ApiItemResponse<LoginDtoResponse> login(String token)
            throws JOSEException {
            String email = googleTokenUtil.getEmail(token);
            User user = userRepository.findByEmailAndTypeRegisterAndStatus(email, Categorize.GOOGLE, Categorize.VERIFIED)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String refreshToken = tokenUtil.generateRefreshToken(user);
        //Save refresh-token to db
        tokenService.save(tokenUtil.createTokenEntity(refreshToken, Categorize.ACTIVE));
        //return access-token and refresh-token
        return apiResponseBuilder.buildResponse(new LoginDtoResponse(tokenUtil.generateAccessToken(user), refreshToken),
                HttpStatus.OK);
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
        User user = createRegisterUser(registerDtoRequest.username(), registerDtoRequest.password(),
                registerDtoRequest.email(), Categorize.UNVERIFIED, Categorize.SYSTEM, "");
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
        User user = createRegisterUser(request.username(), UUID.randomUUID().toString(), email, Categorize.VERIFIED,
                Categorize.GOOGLE, "default");
        userRepository.save(user);
        return apiResponseBuilder.buildResponse(HttpStatus.CREATED, "Create account successfully!");
    }

    @Override
    public ApiItemResponse<String> verifyEmail(String token) {
        User user = userRepository.findByUsername(tokenUtil.getUsername(token))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(Categorize.VERIFIED);
        userRepository.save(user);
        //de-active token
        tokenService.save(tokenUtil.createTokenEntity(token, Categorize.INACTIVE));
        return apiResponseBuilder.buildResponse(HttpStatus.OK, "Email verified!");
    }

    @Override
    public ApiItemResponse<String> logout(String token) {
        //inactive refresh token
        tokenService.inactive(UUID.fromString(tokenUtil.getJwtId(token)));
        return apiResponseBuilder.buildResponse(HttpStatus.OK, "Logout successfully!");
    }

    @Override
    public ApiItemResponse<LoginDtoResponse> getAccessToken(String token) throws JOSEException {
        String id = tokenUtil.getJwtId(token);
        if (!tokenService.existsByIdAndStatus(UUID.fromString(id), Categorize.ACTIVE))
            throw new AppException(ErrorCode.INVALID_TOKEN);
        tokenService.inactive(UUID.fromString(id));
        //Get user info from token
        User user = tokenUtil.getUser(token);
        String refreshToken = tokenUtil.generateRefreshToken(user);
        //Save refresh-token to db
        tokenService.save(tokenUtil.createTokenEntity(refreshToken, Categorize.ACTIVE));
        //return new access-token, refresh-token
        return apiResponseBuilder.buildResponse(new LoginDtoResponse(tokenUtil.generateAccessToken(user), refreshToken),
                HttpStatus.OK);
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
        return apiResponseBuilder.buildResponse(HttpStatus.OK, "Password changed successfully!");
    }

    @Override
    public ApiItemResponse<String> forgotPassword(String email) throws MessagingException, JOSEException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        emailUtil.sendResetPassword(user.getEmail(), user.getUsername(),
                tokenUtil.generateAccessToken(user));
        return apiResponseBuilder.buildResponse(HttpStatus.OK, "Please check your email to reset password!");
    }

    @Override
    public ApiItemResponse<String> resetPassword(ResetPasswordDtoRequest resetPasswordDtoRequest) {
        if (!resetPasswordDtoRequest.newPassword().equals(resetPasswordDtoRequest.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        String token = resetPasswordDtoRequest.token();
        String username = tokenUtil.getUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(resetPasswordDtoRequest.newPassword()));
        userRepository.save(user);
        tokenService.save(tokenUtil.createTokenEntity(token, Categorize.INACTIVE));
        return apiResponseBuilder.buildResponse(HttpStatus.OK, "Password reset successfully!");
    }

    @Override
    public ApiItemResponse<UserDto> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserDto userDto = new UserDto(user.getUsername(), user.getEmail(), null, null, null,
                user.getAvatar(), user.getRating(), user.getReputation(),
                null, null, null, null);
        return apiResponseBuilder.buildResponse(userDto, HttpStatus.OK);
    }

    @Override
    public ApiListResponse<UserDto> getUsers(String search, int page, int size, Sort.Direction direction, String... properties) {
        Page<User> users = userRepository.findByUsernameContainsIgnoreCase(search, pagingUtil
                .getPageable(User.class, page, size, direction, properties));
        //Parse to UserDto
        List<UserDto> list = users.getContent().stream()
                .map(user -> new UserDto(user.getUsername(), user.getEmail(),
                        user.getRole(), user.getStatus(), user.getTypeRegister(),
                        user.getAvatar(), user.getRating(), user.getReputation(),
                        user.getCreatedBy(), user.getCreatedAt(), user.getUpdatedBy(), user.getUpdatedAt()))
                .toList();
        return apiResponseBuilder.buildResponse(list, users.getSize(), users.getNumber() + 1,
                users.getTotalElements(), users.getTotalPages(), HttpStatus.OK);
    }

    @Override
    public ApiItemResponse<UserDto> getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserDto userDto = new UserDto(user.getUsername(), user.getEmail(), null, null, null,
                user.getAvatar(), user.getRating(), user.getReputation(),
                null, null, null, null);
        return apiResponseBuilder.buildResponse(userDto, HttpStatus.OK);
    }

    @Override
    public ApiItemResponse<String> deleteUser(String username) {
        Optional<User> removedUser = userRepository.findByUsernameAndStatus(username, Categorize.REMOVED);
        if (removedUser.isPresent()) {
            throw new AppException(ErrorCode.USER_DELETED_BEFORE);
        }
        List<Categorize> validStatuses = Arrays.asList(Categorize.VERIFIED, Categorize.UNVERIFIED);
        User user = userRepository.findByUsernameAndStatusIn(username, validStatuses)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(Categorize.REMOVED);
        userRepository.save(user);
        return apiResponseBuilder.buildResponse(HttpStatus.OK, "User deleted successfully!");
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
        return apiResponseBuilder.buildResponse(user, HttpStatus.CREATED);
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

    @Override
    public boolean updateReputation(int reputation, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setReputation(reputation);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateRating(float rating, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setRating(rating);
        userRepository.save(user);
        return true;
    }

    @Override
    public ApiItemResponse<UserDto> updateAvatar(String avatar) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setAvatar(avatar);
        userRepository.save(user);
        UserDto userDto = new UserDto(user.getUsername(), user.getEmail(), null, null, null,
                user.getAvatar(), user.getRating(), user.getReputation(),
                null, null, null, null);
        return apiResponseBuilder.buildResponse(userDto, HttpStatus.OK);
    }

    @Override
    public ApiItemResponse<User> updateUser(String username, User user) {
        User userUpdate = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!userUpdate.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (!userUpdate.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        userUpdate.setUsername(user.getUsername());
        userUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userUpdate.setEmail(user.getEmail());
        userUpdate.setRole(user.getRole());
        userUpdate.setStatus(user.getStatus());
        userUpdate.setTypeRegister(user.getTypeRegister());
        userUpdate.setAvatar(user.getAvatar());
        userUpdate.setRating(user.getRating());
        userUpdate.setReputation(user.getReputation());
        userRepository.save(userUpdate);
        return apiResponseBuilder.buildResponse(userUpdate, HttpStatus.OK);
    }

    @Override
    public ApiItemResponse<String> banUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(Categorize.BANNED);
        userRepository.save(user);
        return apiResponseBuilder.buildResponse(HttpStatus.OK, "User banned successfully!");
    }

    @Override
    public void saveUser(User user) {
        user.setStatus(Categorize.ONLINE);
        userRepository.save(user);
    }

    @Override
    public void disconnect(User user) {
        var storedUser = userRepository.findById(user.getId()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Categorize.OFFLINE);
            userRepository.save(storedUser);
        }
    }

    @Override
    public ApiListResponse<UserDtoWebSocket> findConnectedUsers(int page, int size, Sort.Direction direction, String... properties) {
        Page<User> users = userRepository.findAllByStatus(Categorize.ONLINE, pagingUtil
                .getPageable(User.class, page, size, direction, properties));

        return apiResponseBuilder.buildResponse(
                parseToList(users),
                users.getSize(),
                users.getNumber() + 1,
                users.getTotalElements(),
                users.getTotalPages(),
                HttpStatus.OK,
                null
        );
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private List<UserDtoWebSocket> parseToList(Page<User> users) {
        return users.getContent().stream()
                .map(user -> new UserDtoWebSocket(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getEmail(),
                        user.getRole(),
                        user.getStatus(),
                        user.getTypeRegister(),
                        user.getAvatar(),
                        user.getRating(),
                        user.getReputation(),
                        user.getCreatedBy(),
                        user.getCreatedAt(),
                        user.getUpdatedBy(),
                        user.getUpdatedAt()
                ))
                .toList();
    }

    private User createRegisterUser(String username, String password, String email,
                                    Categorize status, Categorize typeRegister, String avatar) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(Categorize.MEMBER);
        user.setStatus(status);
        user.setTypeRegister(typeRegister);
        user.setAvatar(avatar);
        user.setCreatedBy(username);
        user.setUpdatedBy(username);
        return user;
    }
}
