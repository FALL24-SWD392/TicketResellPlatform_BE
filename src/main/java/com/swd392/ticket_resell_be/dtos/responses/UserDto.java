package com.swd392.ticket_resell_be.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        String username,
        String email,
        Categorize role,
        Categorize status,
        Categorize typeRegister,
        String avatar,
        float rating,
        int reputation,
        String createdBy,
        Date createdAt,
        String updatedBy,
        Date updatedAt
) {
}
