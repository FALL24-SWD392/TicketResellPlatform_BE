package com.swd392.ticket_resell_be.enums;

public enum Categorize {
    //User's status
    INACTIVE,
    ACTIVE,
    BANNED,
    REMOVED,
    RESTRICTED,
    VERIFIED,
    UNVERIFIED,
    //User's role
    MEMBER,
    STAFF,
    ADMIN,
    //User's type register
    SYSTEM,
    GOOGLE,

    //General Status
    PENDING,
    APPROVED,
    REJECTED,
    COMPLETED,
    CANCEL,
    SUCCESS,
    FAILED,

    //Ticket Status
    TRADING,
    EXPIRED,
    SOLD,

    //Ticket Type
    MOVIE,
    EVENT,
    VOUCHER,
    TRANSPORTATION,
    AMUSEMENT,
    TOURIST,
    ALL,

    //
}
