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
    USER_ALREADY_EXISTS("User already exists", HttpStatus.CONFLICT),
    WRONG_PASSWORD("Wrong password", HttpStatus.BAD_REQUEST),
    USERNAME_EMPTY("Username cannot be empty", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY("Password cannot be empty", HttpStatus.BAD_REQUEST),
    PASSWORD_LENGTH("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST),
    //Ticket's Error
    //Transaction's Error
    INVALID_AMOUNT("Invalid amount provided", HttpStatus.BAD_REQUEST),
    INVALID_SUBSCRIPTION("Invalid subscription provided", HttpStatus.BAD_REQUEST),
    TRANSACTION_NOT_FOUND("Transaction not found", HttpStatus.NOT_FOUND),
    //Package's Error
    PACKAGE_NOT_FOUND("Package not found", HttpStatus.NOT_FOUND),
    INVALID_PACKAGE("Invalid package", HttpStatus.BAD_REQUEST),
    //Subscription's Error
    SUBSCRIPTION_NOT_FOUND("Subscription not found", HttpStatus.NOT_FOUND),
    INVALID_TRANSACTION("Transaction not found", HttpStatus.NOT_FOUND),
    PAYMENT_FAILED("Payement failed", HttpStatus.BAD_REQUEST),
    TRANSACTION_ALREADY_CONFIRMED("Transaction Already Confirmed", HttpStatus.OK);
    String message;
    HttpStatus status;
}
