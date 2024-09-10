package com.swd392.ticket_resell_be.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ApiResponseBuilderTest {
    @InjectMocks
    ApiResponseBuilder apiResponseBuilder;

    @Test
    void testBuildResponse_ApiItemResponse_StatusMessage() {
        //when
        var response = apiResponseBuilder.buildResponse(HttpStatus.OK, "message");
        //then
        assertEquals(HttpStatus.OK, response.status());
        assertEquals("message", response.message());
        assertNull(response.data());
    }

    @Test
    void testBuildResponse_ApiItemResponse_DataStatus() {
        //when
        var response = apiResponseBuilder.buildResponse("data", HttpStatus.OK);
        //then
        assertEquals("data", response.data());
        assertEquals(HttpStatus.OK, response.status());
        assertNull(response.message());
    }

    @Test
    void testBuildResponse_ApiItemResponse_DataStatusMessage() {
        //when
        var response = apiResponseBuilder.buildResponse("data", HttpStatus.OK, "message");
        //then
        assertEquals("data", response.data());
        assertEquals(HttpStatus.OK, response.status());
        assertEquals("message", response.message());
    }
}
