package com.swd392.ticket_resell_be.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.swd392.ticket_resell_be.entities.Token;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
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
                .claim("id", user.getId().toString())
                .claim("email", user.getEmail())
                .claim("avatar", user.getAvatar())
                .claim("rating", user.getRating())
                .claim("reputation", user.getReputation())
                .claim("scope", user.getRole().name())
                .claim("status", user.getStatus().name())
                .claim("typeRegister", user.getTypeRegister().name())
                .claim("createdBy", user.getCreatedBy())
                .claim("createdAt", user.getCreatedAt())
                .claim("updatedBy", user.getUpdatedBy())
                .claim("updatedAt", user.getUpdatedAt())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        //Sign jwt
        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(secretKey.getBytes()));
        return jwsObject.serialize();
    }

    public String getUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Token createTokenEntity(String token, Categorize status) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String id = signedJWT.getJWTClaimsSet().getJWTID();
            String username = signedJWT.getJWTClaimsSet().getSubject();
            Date expAt = signedJWT.getJWTClaimsSet().getExpirationTime();

            Token result = new Token();
            result.setId(UUID.fromString(id));
            result.setExpAt(expAt);
            result.setStatus(status);
            result.setCreatedBy(username);
            result.setUpdatedBy(username);
            return result;
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getJwtId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public User getUser(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            User user = new User();
            user.setId(UUID.fromString(signedJWT.getJWTClaimsSet().getClaim("id").toString()));
            user.setUsername(signedJWT.getJWTClaimsSet().getSubject());
            user.setEmail(signedJWT.getJWTClaimsSet().getClaim("email").toString());
            user.setAvatar(signedJWT.getJWTClaimsSet().getClaim("avatar").toString());
            user.setRating(Float.parseFloat(signedJWT.getJWTClaimsSet().getClaim("rating").toString()));
            user.setReputation(Integer.parseInt(signedJWT.getJWTClaimsSet().getClaim("reputation").toString()));
            user.setRole(Categorize.valueOf(signedJWT.getJWTClaimsSet().getClaim("scope").toString()));
            user.setStatus(Categorize.valueOf(signedJWT.getJWTClaimsSet().getClaim("status").toString()));
            user.setTypeRegister(Categorize.valueOf(signedJWT.getJWTClaimsSet().getClaim("typeRegister").toString()));
            user.setCreatedBy(signedJWT.getJWTClaimsSet().getClaim("createdBy").toString());
            user.setCreatedAt(new Date(Long.parseLong(signedJWT.getJWTClaimsSet().getClaim("createdAt").toString())));
            user.setUpdatedBy(signedJWT.getJWTClaimsSet().getClaim("updatedBy").toString());
            user.setUpdatedAt(new Date(Long.parseLong(signedJWT.getJWTClaimsSet().getClaim("updatedAt").toString())));
            return user;
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
