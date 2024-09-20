package com.swd392.ticket_resell_be.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.swd392.ticket_resell_be.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenUtil {
    private final JwtDecoder jwtDecoder;
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
    @Value("${ACCESS_TOKEN_EXP}")
    private long accessTokenExp;
    @Value("${REFRESH_TOKEN_EXP}")
    private long refreshTokenExp;

    public String generateAccessToken(User user) throws JOSEException {
        return generateToken(user, accessTokenExp);
    }

    public String generateRefreshToken(User user) throws JOSEException {
        return generateToken(user, refreshTokenExp);
    }

    private String generateToken(User user, long expTime) throws JOSEException {
        //Build jwt header
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
        //Build jwt payload
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("swd392.com")
                .issueTime(new Date(System.currentTimeMillis()))
                .expirationTime(new Date(System.currentTimeMillis() + expTime))
                .subject(user.getUsername())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        //Sign jwt
        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(secretKey.getBytes()));
        return jwsObject.serialize();
    }

    public String getUsernameFromToken(String token) {
        return jwtDecoder.decode(token).getClaim("sub");
    }
}
