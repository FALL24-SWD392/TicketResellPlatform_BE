package com.swd392.ticket_resell_be.controllers;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.ChangePasswordDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.RegisterDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.ResetPasswordDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.LoginDtoResponse;
import com.swd392.ticket_resell_be.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    UserService userService;

    @GetMapping("/login")
    public ResponseEntity<ApiItemResponse<LoginDtoResponse>> login(@RequestBody @Valid
                                                                   LoginDtoRequest loginDtoRequest)
            throws JOSEException {
        return ResponseEntity.ok(userService.login(loginDtoRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiItemResponse<String>> register(@RequestBody @Valid
                                                            RegisterDtoRequest registerDtoRequest)
            throws JOSEException, MessagingException {
        return ResponseEntity.ok(userService.register(registerDtoRequest));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiItemResponse<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(userService.verifyEmail(token));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiItemResponse<String>> logout(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER).substring(7);
        return ResponseEntity.ok(userService.logout(token));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ApiItemResponse<String>> refreshToken(HttpServletRequest request)
            throws JOSEException {
        String token = request.getHeader(AUTHORIZATION_HEADER).substring(7);
        return ResponseEntity.ok(userService.refreshToken(token));
    }

    @PutMapping("change-password")
    public ResponseEntity<ApiItemResponse<String>> changePassword(@RequestBody @Valid
                                                                  ChangePasswordDtoRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<ApiItemResponse<String>> forgotPassword(@RequestBody String email)
            throws MessagingException, JOSEException {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ApiItemResponse<String>> resetPassword(HttpServletRequest request, @RequestBody @Valid
    ResetPasswordDtoRequest resetPasswordDtoRequest)
            throws JOSEException {
        String token = request.getHeader(AUTHORIZATION_HEADER).substring(7);
        return ResponseEntity.ok(userService.resetPassword(token, resetPasswordDtoRequest));
    }
}
