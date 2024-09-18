package com.swd392.ticket_resell_be.services;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;

public interface UserService {
    ApiItemResponse<String> login(LoginDtoRequest loginDtoRequest) throws JOSEException;
}
