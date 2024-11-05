package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.BanUserRequest;
import com.swd392.ticket_resell_be.dtos.requests.DeleteUserRequest;
import com.swd392.ticket_resell_be.dtos.requests.UpdateAvatarRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.UserDto;
import com.swd392.ticket_resell_be.dtos.responses.UserDtoWebSocket;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public ResponseEntity<ApiListResponse<UserDto>>
    getUsers(@RequestParam(defaultValue = "") String search,
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(defaultValue = "20") int size,
             @RequestParam(defaultValue = "ASC") Sort.Direction direction,
             @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(userService.getUsers(search, page - 1, size, direction, properties));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiItemResponse<UserDto>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiItemResponse<String>> deleteUser(@RequestBody DeleteUserRequest username) {
        return ResponseEntity.ok(userService.deleteUser(username.username()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiItemResponse<User>> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiItemResponse<String>> banUser(@RequestBody BanUserRequest username) {
        return ResponseEntity.ok(userService.banUser(username.username()));
    }

    @PutMapping("/myInfo")
    public ResponseEntity<ApiItemResponse<UserDto>> updateAvatar(@RequestBody UpdateAvatarRequest avatar) {
        return ResponseEntity.ok(userService.updateAvatar(avatar.avatar()));
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiItemResponse<User>> updateUser(@PathVariable String username, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(username, user));
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public User addUser(@Payload User user) {
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public User disconnectUser(@Payload User user) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/connected-users")
    public ResponseEntity<ApiListResponse<UserDtoWebSocket>> findConnectedUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties
    ) {
        return ResponseEntity.ok(userService.findConnectedUsers(page - 1, size, direction, properties));
    }
}
