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
    USER_DELETED_BEFORE("User are deleted before", HttpStatus.NOT_FOUND),

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
    TOKEN_NOT_FOUND("Token not found", HttpStatus.NOT_FOUND),
    GOOGLE_TOKEN_EMPTY("Google token not empty", HttpStatus.BAD_REQUEST),
    //Ticket's Error
    TICKET_NOT_FOUND("Ticket not found", HttpStatus.NOT_FOUND),

    //Feedback error
    FEEDBACK_NOT_FOUND("Feedback not found", HttpStatus.NOT_FOUND),
    USER_HAVE_NOT_BUY_YET("User has not buyed yet", HttpStatus.CONFLICT),

    //Report error
    REPORT_NOT_FOUND("Report not found", HttpStatus.NOT_FOUND),
    REPORT_NOT_IN_PENDING_STATE("Report not in pending state", HttpStatus.ALREADY_REPORTED),
    USER_HAVE_NOT_YET_TRANSACTED("User has not yet transacted", HttpStatus.CONFLICT),
    INVALID_STATUS("Invalid status", HttpStatus.CONFLICT),

    //ChatBox error
    CHAT_BOX_NOT_FOUND("Chat box not found", HttpStatus.NOT_FOUND),

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
    INSUFFICIENT_REPUTATION("You do not have enough reputation points to purchase this subscription.", HttpStatus.BAD_REQUEST),

    //Chat error
    CHAT_BOX_DOES_NOT_EXIST("Chat Box not found", HttpStatus.NOT_FOUND),

    //Order error
    ORDER_DOES_NOT_EXIST("Order not found", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_DOES_NOT_EXIST("Order detail not found", HttpStatus.NOT_FOUND);

    String message;
    HttpStatus status;
}
