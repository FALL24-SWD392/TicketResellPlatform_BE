package com.swd392.ticket_resell_be.utils;

import com.google.auth.oauth2.TokenVerifier;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenUtil {
    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    public String getEmail(String token) {
        TokenVerifier verifier = TokenVerifier.newBuilder()
                .setAudience(clientId)
                .setIssuer("https://accounts.google.com")
                .build();
        try {
            return verifier.verify(token).getPayload().get("email").toString();
        } catch (TokenVerifier.VerificationException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
