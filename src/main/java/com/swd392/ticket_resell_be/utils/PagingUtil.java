package com.swd392.ticket_resell_be.utils;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PagingUtil {

    public Pageable getPageable(PageDtoRequest pageDtoRequest) {
        return PageRequest.of(pageDtoRequest.page(), pageDtoRequest.size());
    }
}
