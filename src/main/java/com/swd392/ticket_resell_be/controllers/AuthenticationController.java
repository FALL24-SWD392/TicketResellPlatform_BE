package com.swd392.ticket_resell_be.controllers;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.*;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.LoginDtoResponse;
import com.swd392.ticket_resell_be.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
@Tag(name = "Authentication APIs")
public class AuthenticationController {
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiItemResponse<LoginDtoResponse>> login(@RequestBody @Valid LoginDtoRequest request)
            throws JOSEException {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/login/google")
    public ResponseEntity<ApiItemResponse<LoginDtoResponse>> login(@RequestBody String token)
            throws JOSEException {
        return ResponseEntity.ok(userService.login(token));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiItemResponse<String>> register(@RequestBody @Valid RegisterDtoRequest request)
            throws JOSEException, MessagingException {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/register/google")
    public ResponseEntity<ApiItemResponse<String>> register(@RequestBody @Valid RegisterGoogleDtoRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/email/verify")
    public ResponseEntity<ApiItemResponse<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(userService.verifyEmail(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiItemResponse<String>> logout(@RequestBody String token) {
        return ResponseEntity.ok(userService.logout(token));
    }

    @PostMapping("/access-token")
    public ResponseEntity<ApiItemResponse<LoginDtoResponse>> getAccessToken(@RequestBody String token)
            throws JOSEException {
        return ResponseEntity.ok(userService.getAccessToken(token));
    }

    @PutMapping("/password/change")
    public ResponseEntity<ApiItemResponse<String>> changePassword(@RequestBody @Valid ChangePasswordDtoRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @PutMapping("/password/forgot")
    public ResponseEntity<ApiItemResponse<String>> forgotPassword(@RequestBody String email)
            throws MessagingException, JOSEException {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PutMapping("/password/reset")
    public ResponseEntity<ApiItemResponse<String>> resetPassword(@RequestBody @Valid ResetPasswordDtoRequest dtoRequest)
            throws JOSEException {
        return ResponseEntity.ok(userService.resetPassword(dtoRequest));
    }
}
