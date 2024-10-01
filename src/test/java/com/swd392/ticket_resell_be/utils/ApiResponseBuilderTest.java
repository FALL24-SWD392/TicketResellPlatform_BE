package com.swd392.ticket_resell_be.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ApiResponseBuilderTest {
    @InjectMocks
    ApiResponseBuilder apiResponseBuilder;

    @Test
    void testBuildResponse_ApiItemResponse() {
        //when
        var response = apiResponseBuilder.buildResponse("data", HttpStatus.OK, "message");
        //then
        assertEquals("data", response.data());
        assertEquals(HttpStatus.OK, response.status());
        assertEquals("message", response.message());
    }
}
