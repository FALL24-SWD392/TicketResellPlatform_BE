package com.swd392.ticket_resell_be.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApiItemResponse<T> {
    T data;
    String message;
    HttpStatus status;
}
