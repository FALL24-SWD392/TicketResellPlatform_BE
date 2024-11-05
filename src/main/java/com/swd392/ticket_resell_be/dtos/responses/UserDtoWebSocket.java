package com.swd392.ticket_resell_be.dtos.responses;

import com.swd392.ticket_resell_be.enums.Categorize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoWebSocket {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private Categorize role;
    private Categorize status;
    private Categorize typeRegister;
    private String avatar;
    private float rating;
    private int reputation;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
}
