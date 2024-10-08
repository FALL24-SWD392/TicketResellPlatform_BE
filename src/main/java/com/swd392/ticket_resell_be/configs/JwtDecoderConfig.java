package com.swd392.ticket_resell_be.configs;

import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.services.TokenService;
import com.swd392.ticket_resell_be.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class JwtDecoderConfig implements JwtDecoder {
    private final TokenService tokenService;
    private final TokenUtil tokenUtil;
    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKey;
    private NimbusJwtDecoder jwtDecoder;

    @Bean
    public JwtDecoder jwtDecoder() {
        jwtDecoder = NimbusJwtDecoder
                .withSecretKey(new SecretKeySpec(jwtSecretKey.getBytes(), "HS256"))
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        return jwtDecoder;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        String id = tokenUtil.getJwtId(token);
        if (tokenService.existsByIdAndStatus(UUID.fromString(id), Categorize.INACTIVE)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return jwtDecoder.decode(token);
    }
}
