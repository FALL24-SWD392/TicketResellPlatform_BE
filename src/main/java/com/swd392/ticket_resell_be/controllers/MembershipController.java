package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.MembershipDtoResponse;
import com.swd392.ticket_resell_be.services.MembershipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memberships")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MembershipController {

    MembershipService membershipService;

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public ApiItemResponse<MembershipDtoResponse> getMembershipForLoggedInUser() {
        return membershipService.getMembershipForUser();
    }
}
