package com.swd392.ticket_resell_be.services;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.impls.UserServiceImplement;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.TokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserServiceImplement memberServiceImplement;
    @Mock
    UserRepository userRepository;
    @Mock
    TokenUtil tokenUtil;
    @Mock
    ApiResponseBuilder apiResponseBuilder;

    @Test
    void testLogin_Success() throws JOSEException {
        //given
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("test")
                .password("12345678")
                .build();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("test", "12345678");
        ApiItemResponse<String> apiItemResponse = ApiItemResponse.<String>builder()
                .data("token")
                .status(HttpStatus.OK)
                .build();
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(tokenUtil.generateToken(user)).thenReturn("token");
        when(apiResponseBuilder.buildResponse(anyString(), any(HttpStatus.class))).thenReturn(apiItemResponse);
        ApiItemResponse<?> response = memberServiceImplement.login(loginDtoRequest);
        //then
        assertEquals("token", response.data());
        assertEquals(HttpStatus.OK, response.status());
    }

    @Test
    void testLogin_UserNotFound() {
        //given
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("test", "12345678");
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(AppException.class,
                () -> memberServiceImplement.login(loginDtoRequest),
                ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void testLogin_WrongPassword() {
        //given
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("test")
                .password("12345678")
                .build();
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("test", "12345679");
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        //then
        assertThrows(AppException.class,
                () -> memberServiceImplement.login(loginDtoRequest),
                ErrorCode.WRONG_PASSWORD.getMessage());
    }
}
