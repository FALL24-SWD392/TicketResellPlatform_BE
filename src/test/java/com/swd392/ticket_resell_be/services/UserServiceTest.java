package com.swd392.ticket_resell_be.services;

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
import com.swd392.ticket_resell_be.services.impls.UserServiceImplement;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.EmailUtil;
import com.swd392.ticket_resell_be.utils.TokenUtil;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserServiceImplement userServiceImplement;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    ApiResponseBuilder apiResponseBuilder;
    @Mock
    TokenUtil tokenUtil;
    @Mock
    EmailUtil emailUtil;

    private User admin;
    private User member;

    @BeforeEach
    void setUp() {
        admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setUsername("admin");
        admin.setPassword("password");
        admin.setEmail("admin@gmail.com");
        admin.setRole(Categorize.ADMIN);
        admin.setStatus(Categorize.VERIFIED);
        admin.setTypeRegister(Categorize.SYSTEM);

        member = new User();
        member.setId(UUID.randomUUID());
        member.setUsername("member");
        member.setPassword("password");
        member.setEmail("member@gmail.com");
        member.setRole(Categorize.MEMBER);
        member.setStatus(Categorize.VERIFIED);
        member.setTypeRegister(Categorize.SYSTEM);
    }

    @Test
    void testLogin_Success() throws JOSEException {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("admin", "password");
        LoginDtoResponse loginDtoResponse = new LoginDtoResponse("accessToken", "refreshToken");
        ApiItemResponse<LoginDtoResponse> apiItemResponse = new ApiItemResponse<>(loginDtoResponse,
                HttpStatus.OK, null);
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenUtil.generateAccessToken(admin)).thenReturn("accessToken");
        when(tokenUtil.generateRefreshToken(admin)).thenReturn("refreshToken");
        when(apiResponseBuilder.buildResponse(loginDtoResponse, HttpStatus.OK, null))
                .thenReturn(apiItemResponse);
        ApiItemResponse<LoginDtoResponse> response = userServiceImplement.login(loginDtoRequest);
        //then
        assertEquals(apiItemResponse, response);
    }

    @Test
    void testLogin_UserNotFound() {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("admin", "password");
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(AppException.class, () -> userServiceImplement.login(loginDtoRequest),
                ErrorCode.USER_NOT_FOUND.name());
    }

    @Test
    void testLogin_WrongPassword() {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("admin", "password");
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        //then
        assertThrows(AppException.class, () -> userServiceImplement.login(loginDtoRequest),
                ErrorCode.WRONG_PASSWORD.name());
    }

    @Test
    void testRegister_Success() throws JOSEException, MessagingException {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("user",
                        "password",
                        "password",
                        "member@gmail.com");
        ApiItemResponse<String> apiItemResponse = new ApiItemResponse<>("Please verify your email!",
                HttpStatus.CREATED, null);
        member.setStatus(Categorize.UNVERIFIED);
        //when
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(member);
        when(tokenUtil.generateAccessToken(any(User.class))).thenReturn("token");
        when(apiResponseBuilder.buildResponse("Please verify your email!",
                HttpStatus.CREATED, null)).thenReturn(apiItemResponse);
        ApiItemResponse<String> response = userServiceImplement.register(registerDtoRequest);
        //then
        assertEquals(apiItemResponse, response);
        verify(emailUtil, times(1))
                .sendVerifyEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testRegister_PasswordNotMatch() {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("user",
                        "password",
                        "password1",
                        "member@gmail.com");
        //then
        assertThrows(AppException.class, () -> userServiceImplement.register(registerDtoRequest),
                ErrorCode.PASSWORD_NOT_MATCH.name());
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("user",
                        "password",
                        "password",
                        "member@gmail.com");
        //when
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        //then
        assertThrows(AppException.class, () -> userServiceImplement.register(registerDtoRequest),
                ErrorCode.USERNAME_ALREADY_EXISTS.name());
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("user",
                        "password",
                        "password",
                        "member@gmail.com");
        //when
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        //then
        assertThrows(AppException.class, () -> userServiceImplement.register(registerDtoRequest),
                ErrorCode.EMAIL_ALREADY_EXISTS.name());
    }

    @Test
    void testVerifyEmail_Success() {
        //given
        String token = "token";
        ApiItemResponse<String> apiItemResponse = new ApiItemResponse<>("Email verified!",
                HttpStatus.OK, null);
        //when
        when(tokenUtil.getUsernameFromToken(anyString())).thenReturn("user");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(apiResponseBuilder.buildResponse("Email verified!",
                HttpStatus.OK, null)).thenReturn(apiItemResponse);
        ApiItemResponse<String> response = userServiceImplement.verifyEmail(token);
        //then
        assertEquals(apiItemResponse, response);
    }

    @Test
    void testVerifyEmail_UserNotFound() {
        //given
        String token = "token";
        //when
        when(tokenUtil.getUsernameFromToken(anyString())).thenReturn("user");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(AppException.class, () -> userServiceImplement.verifyEmail(token),
                ErrorCode.USER_NOT_FOUND.name());
    }

}
