package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.entities.BlacklistToken;
import com.swd392.ticket_resell_be.repositories.BlacklistTokenRepository;
import com.swd392.ticket_resell_be.services.BlacklistTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BlacklistTokenServiceImplement implements BlacklistTokenService {
    BlacklistTokenRepository blacklistTokenRepository;

    public void save(BlacklistToken blacklistToken) {
        blacklistTokenRepository.save(blacklistToken);
    }

    @Override
    public boolean existsById(UUID id) {
        return blacklistTokenRepository.existsById(id);
    }
}
