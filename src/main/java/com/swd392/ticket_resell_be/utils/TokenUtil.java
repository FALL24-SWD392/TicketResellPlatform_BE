package com.swd392.ticket_resell_be.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.swd392.ticket_resell_be.entities.BlacklistToken;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenUtil {
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
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim("scope", user.getRole().name())
                .claim("email", user.getEmail())
                .claim("avatar", user.getAvatar())
                .claim("rating", user.getRating())
                .claim("reputation", user.getReputation())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        //Sign jwt
        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(secretKey.getBytes()));
        return jwsObject.serialize();
    }

    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public BlacklistToken generateBlacklistToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String id = signedJWT.getJWTClaimsSet().getJWTID();
            Date expAt = signedJWT.getJWTClaimsSet().getExpirationTime();
            BlacklistToken blacklistToken = new BlacklistToken();
            blacklistToken.setId(UUID.fromString(id));
            blacklistToken.setExpAt(expAt);
            return blacklistToken;
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
