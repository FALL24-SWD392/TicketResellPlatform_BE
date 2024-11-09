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
    RUN_OUT_QUANTITY("Run out quantity", HttpStatus.BAD_REQUEST),

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
    ALREADY_HAVE_SALE_REMAIN("Please post all tickets before buy new subscriptions", HttpStatus.CONFLICT),
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
    ORDER_DETAIL_DOES_NOT_EXIST("Order detail not found", HttpStatus.NOT_FOUND),

    //feedback error
    USER_ALREADY_FEEDBACK_THIS_ORDER("Already feedback this order", HttpStatus.CONFLICT),

    //membership
    YOU_SELL_MAXIMUM_TICKET("You sell maximum ticket", HttpStatus.CONFLICT),
    TICKET_ALREADY_REMOVED("Ticket already removed", HttpStatus.CONFLICT),
    REMAINING_SALES_PRESENT("Cannot purchase a new subscription while there are remaining sales on the current subscription", HttpStatus.CONFLICT),

    //DTO Request
    REPORTED_BY_ID_MUST_NOT_BE_NULL("Reported Id is empty", HttpStatus.BAD_REQUEST),
    TOKEN_EMPTY("Token is empty", HttpStatus.BAD_REQUEST),
    REPORTED_ID_MUST_NOT_BE_NULL("Reporter Id is empty", HttpStatus.BAD_REQUEST),
    CHAT_BOX_ID_MUST_NOT_BE_NULL("Chat Box id is empty", HttpStatus.BAD_REQUEST),
    BUYER_ID_MUST_NOT_BE_NULL("Buyer id is empty", HttpStatus.BAD_REQUEST),
    ORDER_ID_MUST_NOT_BE_NULL("Order id is empty", HttpStatus.BAD_REQUEST),
    FEEDBACK_DESCRIPTION_EMPTY("Feedback description is empty", HttpStatus.BAD_REQUEST),
    RATING_MUST_NOT_BE_NULL("Rating is empty", HttpStatus.BAD_REQUEST),
    STATUS_MUST_NOT_BE_NULL("Status is empty", HttpStatus.BAD_REQUEST),
    SENDER_EMPTY("Sender is empty", HttpStatus.BAD_REQUEST),
    RECIPIENT_EMPTY("Recipient is empty", HttpStatus.BAD_REQUEST),
    TICKET_EMPTY("Ticket is empty", HttpStatus.BAD_REQUEST),
    TICKET_ID_EMPTY("Ticket id is empty", HttpStatus.BAD_REQUEST),
    QUANTITY_MUST_NOT_BE_NULL("Quantity is empty", HttpStatus.BAD_REQUEST),
    QUANTITY_MUST_BE_GREATER_THAN_0("Quantity must be greater than 0", HttpStatus.BAD_REQUEST),
    REPORT_DESCRIPTION_MUST_NOT_BE_NULL("Report description is empty", HttpStatus.BAD_REQUEST),
    ATTACHMENT_MUST_NOT_BE_NULL("Attachment description is empty", HttpStatus.BAD_REQUEST),
    SELLER_ID_MUST_NOT_BE_NULL("Seller id is empty", HttpStatus.BAD_REQUEST),
    TITLE_EMPTY("Title is empty", HttpStatus.BAD_REQUEST),
    TICKET_EXPIRATION_MUST_NOT_BE_NULL("Ticket expiration time is empty", HttpStatus.BAD_REQUEST),
    EXPIRATION_DATE_MUST_BE_IN_THE_FUTURE("Expiration Date is empty", HttpStatus.BAD_REQUEST),
    TYPE_MUST_NOT_BE_NULL("Type is empty", HttpStatus.BAD_REQUEST),
    SALE_PRICE_MUST_NOT_BE_NULL("Sale price is empty", HttpStatus.BAD_REQUEST),
    SALE_PRICE_NEGATIVE("Sale price is negative", HttpStatus.BAD_REQUEST),
    SALE_PRICE_TOO_HIGH("Sale price is too high", HttpStatus.BAD_REQUEST),
    DESCRIPTION_EMPTY("Description is empty", HttpStatus.BAD_REQUEST),
    IMAGE_EMPTY("Image is empty", HttpStatus.BAD_REQUEST),
    ;
    String message;
    HttpStatus status;
}
