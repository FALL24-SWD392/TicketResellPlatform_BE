package com.swd392.ticket_resell_be.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
        ApiItemResponse<String> apiItemResponse = ApiItemResponse.<String>builder()
                .data("token")
                .status(HttpStatus.OK)
                .build();
        //when
        when(userService.login(loginDtoRequest)).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "12345678");
        ApiItemResponse<Object> apiItemResponse = ApiItemResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("User not found")
                .build();
        //when
        when(userService.login(any())).thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));
        when(apiResponseBuilder.buildResponse(any(HttpStatus.class), anyString())).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_WrongPassword() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "12345678");
        ApiItemResponse<Object> apiItemResponse = ApiItemResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Wrong password")
                .build();
        //when
        when(userService.login(any())).thenThrow(new AppException(ErrorCode.WRONG_PASSWORD));
        when(apiResponseBuilder.buildResponse(any(HttpStatus.class), anyString())).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_UsernameEmpty() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("", "12345678");
        ErrorCode errorCode = ErrorCode.USERNAME_EMPTY;
        ApiItemResponse<Object> apiItemResponse = ApiItemResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
        //when
        when(apiResponseBuilder.buildResponse(any(HttpStatus.class), anyString())).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_PasswordEmpty() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "");
        ErrorCode errorCode = ErrorCode.PASSWORD_EMPTY;
        ApiItemResponse<Object> apiItemResponse = ApiItemResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
        //when
        when(apiResponseBuilder.buildResponse(any(HttpStatus.class), anyString())).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

    @Test
    void testLogin_PasswordLength() throws Exception {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("username", "1234567");
        ErrorCode errorCode = ErrorCode.PASSWORD_LENGTH;
        ApiItemResponse<Object> apiItemResponse = ApiItemResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
        //when
        when(apiResponseBuilder.buildResponse(any(HttpStatus.class), anyString())).thenReturn(apiItemResponse);
        //then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDtoRequest)))
                .andExpect(content().json(objectMapper.writeValueAsString(apiItemResponse)));
    }

}
