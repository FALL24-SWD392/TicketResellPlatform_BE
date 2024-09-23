package com.swd392.ticket_resell_be.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {
    //Undefined errors
    UNDEFINED("Undefined error", HttpStatus.INTERNAL_SERVER_ERROR),
    //User errors
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD("Wrong password", HttpStatus.BAD_REQUEST),
    USERNAME_EMPTY("Username cannot be empty", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY("Password cannot be empty", HttpStatus.BAD_REQUEST),
    PASSWORD_LENGTH("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST),
    EMAIL_EMPTY("Email cannot be empty", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID("Invalid email", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH("Password not match", HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXISTS("Username already exists", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("Email already exists", HttpStatus.CONFLICT),
    //Ticket's Error

    ;
    String message;
    HttpStatus status;
}
