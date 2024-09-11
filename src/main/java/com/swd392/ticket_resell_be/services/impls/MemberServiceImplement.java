package com.swd392.ticket_resell_be.services.impls;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.dtos.requests.LoginDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Member;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.MemberRepository;
import com.swd392.ticket_resell_be.services.MemberService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.TokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MemberServiceImplement implements MemberService {

    MemberRepository memberRepository;
    TokenUtil tokenUtil;
    ApiResponseBuilder apiResponseBuilder;

    @Override
    public ApiItemResponse<String> login(LoginDtoRequest loginDtoRequest) throws JOSEException {
        Member member = memberRepository.findByUsername(loginDtoRequest.username())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!member.getPassword().equals(loginDtoRequest.password()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        return apiResponseBuilder.buildResponse(tokenUtil.generateToken(member), HttpStatus.OK);
    }
}
