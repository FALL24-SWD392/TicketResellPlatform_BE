package com.swd392.ticket_resell_be.services;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.exceptions.AppException;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    ApiItemResponse<String> login(LoginDtoRequest loginDtoRequest) throws JOSEException;

    Optional<User> getUserByName(String username) throws AppException;
}
