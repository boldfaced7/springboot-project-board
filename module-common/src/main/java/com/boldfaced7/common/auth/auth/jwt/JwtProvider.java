package com.boldfaced7.common.auth.auth.jwt;

import com.boldfaced7.common.auth.auth.presentation.response.AuthResponse;
import com.boldfaced7.common.exception.exception.auth.InvalidRefreshTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecretKey()));
    }

    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public AuthResponse extractAuthInfo(String token) {
        Claims payload = parseClaims(token);

        return AuthResponse.builder()
                .memberId(getMemberId(payload))
                .email((String) payload.get("email"))
                .nickname((String) payload.get("nickname"))
                .build();
    }

    public String generateAccessToken(AuthResponse authInfo) {
        return generateToken(authInfo, jwtProperties.getAccessExpiration());
    }

    public String generateRefreshToken(AuthResponse authInfo) {
        return generateToken(authInfo, jwtProperties.getRefreshExpiration());
    }

    public boolean verifyToken(String token) {
        try {
            parseClaims(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String refreshAccessToken(String accessToken, String refreshToken) {
        long fromAccessToken = getMemberId(accessToken, false);
        long fromRefreshToken = getMemberId(refreshToken, true);

        if (fromRefreshToken != fromAccessToken) {
            throw new InvalidRefreshTokenException();
        }
        return generateAccessToken(extractAuthInfo(refreshToken));
    }

    private String generateToken(AuthResponse authInfo, Long expiration) {
        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(jwtProperties.getIssuer())
                .subject(authInfo.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration*1000))
                .claim("memberId", authInfo.getMemberId())
                .claim("email", authInfo.getEmail())
                .claim("nickname", authInfo.getNickname())
                .compact();
    }

    private long getMemberId(String token, boolean expirationCheck) {
        try {
            return getMemberId(parseClaims(token));
        } catch (ExpiredJwtException e) {
            if (expirationCheck) throw new InvalidRefreshTokenException();
            else return getMemberId(e.getClaims());
        } catch (Exception e) {
            throw new InvalidRefreshTokenException();
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    private long getMemberId(Claims payload) {
        return (long) (int) payload.get("memberId");
    }
}
