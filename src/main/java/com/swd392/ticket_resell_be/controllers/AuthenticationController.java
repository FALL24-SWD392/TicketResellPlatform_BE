package com.swd392.ticket_resell_be.controllers;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.*;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.LoginDtoResponse;
import com.swd392.ticket_resell_be.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
@Tag(name = "Authentication APIs")
public class AuthenticationController {
    @Value("${AUTHORIZATION_HEADER}")
    private static String authorizationHeader;
    UserService userService;

    
    @PostMapping("/login/system")
    public ResponseEntity<ApiItemResponse<LoginDtoResponse>> login(@RequestBody @Valid LoginDtoRequest request)
            throws JOSEException {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/login/google")
    public ResponseEntity<ApiItemResponse<LoginDtoResponse>> login(@RequestBody String token)
            throws JOSEException {
        return ResponseEntity.ok(userService.login(token));
    }

    @PostMapping("/register/system")
    public ResponseEntity<ApiItemResponse<String>> register(@RequestBody @Valid RegisterDtoRequest request)
            throws JOSEException, MessagingException {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/register/google")
    public ResponseEntity<ApiItemResponse<String>> register(@RequestBody @Valid RegisterGoogleDtoRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiItemResponse<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(userService.verifyEmail(token));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiItemResponse<String>> logout(HttpServletRequest request) {
        String token = request.getHeader(authorizationHeader).substring(7);
        return ResponseEntity.ok(userService.logout(token));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ApiItemResponse<String>> refreshToken(HttpServletRequest request)
            throws JOSEException {
        String token = request.getHeader(authorizationHeader).substring(7);
        return ResponseEntity.ok(userService.refreshToken(token));
    }

    @PutMapping("change-password")
    public ResponseEntity<ApiItemResponse<String>> changePassword(@RequestBody @Valid ChangePasswordDtoRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<ApiItemResponse<String>> forgotPassword(@RequestBody String email)
            throws MessagingException, JOSEException {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ApiItemResponse<String>> resetPassword(HttpServletRequest request,
                                                                 @RequestBody @Valid
                                                                 ResetPasswordDtoRequest dtoRequest)
            throws JOSEException {
        String token = request.getHeader(authorizationHeader).substring(7);
        return ResponseEntity.ok(userService.resetPassword(token, dtoRequest));
    }
}
