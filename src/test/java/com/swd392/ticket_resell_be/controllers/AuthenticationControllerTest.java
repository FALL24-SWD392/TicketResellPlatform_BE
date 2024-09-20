package com.swd392.ticket_resell_be.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.RegisterDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.LoginDtoResponse;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.exceptions.GlobalExceptionHandler;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(AuthenticationController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        UserService.class,
        ApiResponseBuilder.class,
})
class AuthenticationControllerTest {
    @MockBean
    UserService userService;

    @MockBean
    ApiResponseBuilder apiResponseBuilder;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(new AuthenticationController(userService))
                .setControllerAdvice(new GlobalExceptionHandler(apiResponseBuilder))
                .alwaysExpect(status().isOk())
                .build();
    }

    @Test
    void testLogin_Success() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "12345678");
        LoginDtoResponse loginDtoResponse = new LoginDtoResponse("token", "token");
        ApiItemResponse<LoginDtoResponse> apiItemResponse =
                new ApiItemResponse<>(loginDtoResponse, HttpStatus.OK, null);
        //when
        when(userService.login(loginDtoRequest)).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "12345678");
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, HttpStatus.NOT_FOUND, "User not found");
        //when
        when(userService.login(any())).thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_WrongPassword() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "12345678");
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, HttpStatus.BAD_REQUEST, "Wrong password");
        //when
        when(userService.login(any())).thenThrow(new AppException(ErrorCode.WRONG_PASSWORD));
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_UsernameEmpty() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("", "12345678");
        ErrorCode errorCode = ErrorCode.USERNAME_EMPTY;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_PasswordEmpty() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "");
        ErrorCode errorCode = ErrorCode.PASSWORD_EMPTY;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_PasswordLength() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "1234567");
        ErrorCode errorCode = ErrorCode.PASSWORD_LENGTH;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(get("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_Success() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("username",
                        "password",
                        "password",
                        "member@gmail.com");
        ApiItemResponse<String> apiItemResponse =
                new ApiItemResponse<>("Please verify your email!", HttpStatus.CREATED, null);
        //when
        when(userService.register(any(RegisterDtoRequest.class))).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_PasswordNotMatch() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("username",
                        "password",
                        "password1",
                        "member@gmail.com");
        ErrorCode errorCode = ErrorCode.PASSWORD_NOT_MATCH;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(userService.register(any(RegisterDtoRequest.class)))
                .thenThrow(new AppException(ErrorCode.PASSWORD_NOT_MATCH));
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_UsernameAlreadyExists() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("username",
                        "password",
                        "password",
                        "member@gmail.com");
        ErrorCode errorCode = ErrorCode.USERNAME_ALREADY_EXISTS;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(userService.register(any(RegisterDtoRequest.class))).thenThrow(new AppException(ErrorCode.USERNAME_ALREADY_EXISTS));
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_EmailAlreadyExists() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("username",
                        "password",
                        "password",
                        "member@gmail.com");
        ErrorCode errorCode = ErrorCode.EMAIL_ALREADY_EXISTS;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(userService.register(any(RegisterDtoRequest.class)))
                .thenThrow(new AppException(ErrorCode.EMAIL_ALREADY_EXISTS));
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_PasswordLength() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("username",
                        "1234567",
                        "1234567",
                        "member@gmail.com");
        ErrorCode errorCode = ErrorCode.PASSWORD_LENGTH;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_EmailInvalid() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("username",
                        "12345678",
                        "12345678",
                        "membergmail.com");
        ErrorCode errorCode = ErrorCode.EMAIL_INVALID;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_EmailEmpty() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("username",
                        "12345678",
                        "12345678",
                        "");
        ErrorCode errorCode = ErrorCode.EMAIL_EMPTY;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_PasswordEmpty() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("username",
                        "",
                        "",
                        "member@gmail.com");
        ErrorCode errorCode = ErrorCode.PASSWORD_EMPTY;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testRegister_UsernameEmpty() throws Exception {
        //given
        RegisterDtoRequest registerDtoRequest =
                new RegisterDtoRequest("",
                        "12345678",
                        "12345678",
                        "member@gmail.com");
        ErrorCode errorCode = ErrorCode.USERNAME_EMPTY;
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, errorCode.getStatus(), errorCode.getMessage());
        //when
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testVerifyEmail_Success() throws Exception {
        //given
        String token = "token";
        ApiItemResponse<String> apiItemResponse =
                new ApiItemResponse<>("Email verified!", HttpStatus.OK, null);
        //when
        when(userService.verifyEmail(token)).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(get("/auth/verify-email")
                        .param("token", token))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testVerifyEmail_UserNotFound() throws Exception {
        //given
        String token = "token";
        ApiItemResponse<Object> apiItemResponse =
                new ApiItemResponse<>(null, HttpStatus.NOT_FOUND, "User not found");
        //when
        when(userService.verifyEmail(token)).thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));
        when(apiResponseBuilder.buildResponse(isNull(), any(HttpStatus.class), anyString()))
                .thenReturn(apiItemResponse);
        //then
        mockMvc.perform(get("/auth/verify-email")
                        .param("token", token))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

}
