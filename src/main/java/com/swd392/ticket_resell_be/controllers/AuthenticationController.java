package com.swd392.ticket_resell_be.controllers;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.RegisterDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.LoginDtoResponse;
import com.swd392.ticket_resell_be.services.UserService;
import jakarta.mail.MessagingException;
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
    UserService userService;

    @GetMapping("/login")
    public ResponseEntity<ApiItemResponse<LoginDtoResponse>> login(
            @RequestBody
            @Valid
            LoginDtoRequest loginDtoRequest)
            throws JOSEException {
        return ResponseEntity.ok(userService.login(loginDtoRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiItemResponse<String>> register(@RequestBody
                                                            @Valid
                                                            RegisterDtoRequest registerDtoRequest)
            throws JOSEException, MessagingException {
        return ResponseEntity.ok(userService.register(registerDtoRequest));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiItemResponse<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(userService.verifyEmail(token));
    }

}
