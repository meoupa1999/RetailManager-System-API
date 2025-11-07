package com.sonnh.retailmanagerv2.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // gắn username
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    private SecretKey getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateJwtToken(String token, UserDetails userDetails) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            String username = extractUsername(token);
            if (isTokenExpired(token)) {
                System.out.println("Token out of date");
                return false;
            }
            if (!username.equals(userDetails.getUsername())) {
                System.out.println("token is not match with user in db");
                return false;
            }
            if (!userDetails.isAccountNonLocked()) {
                System.out.println("user has been locked");
                return false;
            }
            return true;
        } catch (ExpiredJwtException ex) {
            System.out.println("token da het han: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("token khong duoc ho tro: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("token sai dinh dang: " + ex.getMessage());
        } catch (SignatureException ex) {
            System.out.println("token sai chu ky: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("token rong hoac sai cu phap: " + ex.getMessage());
        }
        return false;
    }

}
