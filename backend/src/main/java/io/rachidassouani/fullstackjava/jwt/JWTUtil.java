package io.rachidassouani.fullstackjava.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class JWTUtil {

    private final static String SECRET_KEY =
            "rachidassouani.io_123456789_rachidassouani.io_123456789_rachidassouani.io_123456789";

    public String issueToken(String subject, Map<String, Object> claims) {
        String token = Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("https://rachidassouani.io")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(1, DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public String issueToken(String subject) {
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String ...scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String getSubject(String token)  {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}