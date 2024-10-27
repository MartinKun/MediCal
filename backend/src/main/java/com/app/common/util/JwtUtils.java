package com.app.common.util;

import com.app.common.enums.TokenType;
import com.app.exception.InvalidTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    private final int ACCESS_TOKEN_EXPIRATION_IN_MILLIS = 1800000;
    private final int CONFIRM_TOKEN_EXPIRATION_IN_MILLIS = 1800000;
    private final int RESET_PASSWORD_TOKEN_EXPIRATION_IN_MILLIS = 3600000;

    @Value("${security.jwt.accessTokenKey}")
    private String accessTokenKey;

    @Value("${security.jwt.confirmTokenKey}")
    private String confirmTokenKey;

    @Value("${security.jwt.resetPasswordTokenKey}")
    private String resetPasswordTokenKey;

    public String createAccessToken(Authentication authentication) {
        Algorithm algorithm = getAccessTokenAlgorithm();

        String username = authentication.getPrincipal().toString();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_IN_MILLIS))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public String createConfirmToken(String username) {
        Algorithm algorithm = getConfirmTokenAlgorithm();

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + CONFIRM_TOKEN_EXPIRATION_IN_MILLIS))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public String createResetPasswordToken(String username) {
        Algorithm algorithm = getResetPasswordTokenAlgorithm();

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + RESET_PASSWORD_TOKEN_EXPIRATION_IN_MILLIS))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token, TokenType tokenType) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm;
            switch (tokenType) {
                case ACCESS:
                    algorithm = getAccessTokenAlgorithm();
                    break;
                case CONFIRM:
                    algorithm = getConfirmTokenAlgorithm();
                    break;
                case RESET:
                    algorithm = getResetPasswordTokenAlgorithm();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid token type");
            }

            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException();
        }
    }

    private Algorithm getAccessTokenAlgorithm() {
        return Algorithm.HMAC256(this.accessTokenKey);
    }

    private Algorithm getConfirmTokenAlgorithm() {
        return Algorithm.HMAC256(this.confirmTokenKey);
    }

    private Algorithm getResetPasswordTokenAlgorithm() {
        return Algorithm.HMAC256(this.resetPasswordTokenKey);
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }
}
