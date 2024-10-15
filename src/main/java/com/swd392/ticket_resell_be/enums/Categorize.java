package com.swd392.ticket_resell_be.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Categorize {
    //User's role
    MEMBER("role"),
    STAFF("role"),
    ADMIN("role"),

    //User's type register
    SYSTEM("register"),
    GOOGLE("register"),

    //Ticket Type
    MOVIE("ticket"),
    EVENT("ticket"),
    VOUCHER("ticket"),
    TRANSPORTATION("ticket"),
    AMUSEMENT("ticket"),
    TOURIST("ticket"),
    ALL("ticket"),

    //General Status
    SUCCESS("status"),
    FAILED("status"),

    //Status
    UNVERIFIED("status"),
    VERIFIED("status"),
    RESTRICTED("status"),
    BANNED("status"),
    REMOVED("status"),
    PENDING("status"),
    APPROVED("status"),
    TRADING("status"),
    EXPIRED("status"),
    SOLD("status"),
    REJECTED("status"),
    COMPLETED("status"),
    CANCEL("status"),
    ACTIVE("status"),
    INACTIVE("status");

    private final String category;

    Categorize(String category) {
        this.category = category;
    }

    public static List<Categorize> getByCategory(String category) {
        List<Categorize> result = new ArrayList<>();
        for (Categorize value : Categorize.values()) {
            if (value.getCategory().equals(category)) {
                result.add(value);
            }
        }
        return result;
    }
}
