package com.sptech.school.fira_manager_api.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "chave-secreta-jwt";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String gerarToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30 * 60))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String validarToken(String token) {
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
