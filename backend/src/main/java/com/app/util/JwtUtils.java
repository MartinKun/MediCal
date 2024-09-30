package com.app.util;

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

    private final int TOKEN_EXPIRATION_IN_MILLIS = 1800000;

    @Value("${security.jwt.privateKey}")
    private String privateKey;

    public String createToken(Authentication authentication) {
        Algorithm algorithm = getAlgorithm();

        String username = authentication.getPrincipal().toString();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_IN_MILLIS))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = getAlgorithm();

            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(this.privateKey);
    }

    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

}
