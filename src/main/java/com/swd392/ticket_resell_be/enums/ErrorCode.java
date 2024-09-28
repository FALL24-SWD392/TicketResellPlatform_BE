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
    INVALID_TOKEN("Invalid token", HttpStatus.BAD_REQUEST),
    //Ticket's Error
    //Transaction's Error
    AMOUNT_INVALID("Invalid amount provided", HttpStatus.BAD_REQUEST),
    SUBSCRIPTION_INVALID("Invalid subscription provided", HttpStatus.BAD_REQUEST),
    TRANSACTION_NOT_FOUND("Transaction not found", HttpStatus.NOT_FOUND),
    TRANSACTION_CREATION_FAILED("Transaction Creation Fail", HttpStatus.BAD_REQUEST),
    TRANSACTION_DATA_INVALID("Invalid Transaction Data", HttpStatus.BAD_REQUEST),

    //Subscription's Error
    SUBSCRIPTION_NOT_FOUND("Subscription not found", HttpStatus.NOT_FOUND),
    INVALID_SUBSCRIPTION("Invalid package", HttpStatus.BAD_REQUEST),
    //Membership's Error
    MEMBERSHIP_NOT_FOUND("Membership not found", HttpStatus.NOT_FOUND),
    //Payment's Error
    PAYMENT_FAILED("Payment failed", HttpStatus.BAD_REQUEST),

    USER_SUBSCRIPTION_NOT_FOUND("User or Subscription not found", HttpStatus.NOT_FOUND),
    INSUFFICIENT_REPUTATION("You do not have enough reputation points to purchase this subscription.", HttpStatus.BAD_REQUEST )
    ;
    String message;
    HttpStatus status;
}
