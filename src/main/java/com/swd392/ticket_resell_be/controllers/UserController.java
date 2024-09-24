package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @GetMapping("/current")
    public ResponseEntity<ApiItemResponse<Object>> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping
    public ResponseEntity<ApiListResponse<User>> getUsers(@RequestBody PageDtoRequest pageDtoRequest) {
        return ResponseEntity.ok(userService.getUsers(pageDtoRequest));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiItemResponse<User>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<ApiItemResponse<String>> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    @PostMapping
    public ResponseEntity<ApiItemResponse<User>> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}
