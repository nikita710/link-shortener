package com.example.linkshortner.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtService {
    private final SecretKey secretKey;
    private final String issuer;
    private final long expiration;
    private final TokenRevocationService tokenRevocationService;

    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.issuer}") String issuer, @Value("${app.jwt.expiration}") int expiration, TokenRevocationService tokenRevocationService) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.expiration = expiration;
        this.tokenRevocationService = tokenRevocationService;
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        return Jwts.builder()
                .issuer(issuer)
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration * 60 * 1000))
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        Date issuedAt = claims.getIssuedAt();
        if (issuedAt.toInstant().isBefore(tokenRevocationService.getLastRevocationTime())) {
            throw new JwtException("Token is revoked");
        }
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

    @PreDestroy
    public void onShutdown() throws DestroyFailedException {
        tokenRevocationService.revokeAllToken();
        secretKey.destroy();
        log.info("Token revocation service has been shut down");
    }
}
