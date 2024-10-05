package com.swd392.ticket_resell_be.dtos.responses;

public record UserDto(
        String username,
        String email,
        String avatar,
        float rating,
        int reputation) {
}
