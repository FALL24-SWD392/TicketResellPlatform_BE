package com.swd392.ticket_resell_be.utils;

import com.google.auth.oauth2.TokenVerifier;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoogleTokenUtil {
    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;
    @Value("${ISSUER}")
    private String issuer;

    public String getEmail(String token) {
        TokenVerifier verifier = TokenVerifier.newBuilder()
                .setAudience(clientId)
                .setIssuer(issuer)
                .build();
        try {
            return verifier.verify(token).getPayload().get("email").toString();
        } catch (TokenVerifier.VerificationException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public List<String> getEmailUsernameAvatar(String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return List.of(
                    decodedToken.getEmail(),
                    decodedToken.getName(),
                    decodedToken.getPicture()
            );
        } catch (FirebaseAuthException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
