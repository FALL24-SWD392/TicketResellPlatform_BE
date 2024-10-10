package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.UserDto;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/users")
@Tag(name = "User APIs")
public class UserController {
    UserService userService;

    @GetMapping("/myInfo")
    public ResponseEntity<ApiItemResponse<UserDto>> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiListResponse<UserDto>> getUsers(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        PageDtoRequest pageDtoRequest = new PageDtoRequest(size, page);
        return ResponseEntity.ok(userService.getUsers(pageDtoRequest));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiItemResponse<UserDto>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiItemResponse<String>> deleteUser(@RequestBody String username) {
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiItemResponse<User>> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}
