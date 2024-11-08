package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.entities.Token;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.TokenRepository;
import com.swd392.ticket_resell_be.services.TokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TokenServiceImplement implements TokenService {
    TokenRepository tokenRepository;

    @Override
    public void save(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public void inactive(UUID id) {
        Token tokenEntity = tokenRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_FOUND));
        tokenEntity.setStatus(Categorize.INACTIVE);
        tokenRepository.save(tokenEntity);
    }

    @Override
    public boolean existsByIdAndStatus(UUID id, Categorize status) {
        return tokenRepository.existsByIdAndStatus(id, status);
    }

    @Override
    @Transactional
    public void deleteExpiredToken() {
        tokenRepository.removeByExpAtBefore(new Date());
    }
}
