package com.sptech.school.fira_manager_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Locale;

@Service
public class LoginTentativaService {

    private static final Logger log = LoggerFactory.getLogger(LoginTentativaService.class);

    private static final int MAX_FALHAS = 5;
    private static final Duration TEMPO_BLOQUEIO = Duration.ofMinutes(15);

    private final StringRedisTemplate redis;

    public LoginTentativaService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean estaBloqueado(String email) {
        try {
            return Boolean.TRUE.equals(redis.hasKey(chaveBloqueio(email)));
        } catch (Exception e) {
            log.warn("Redis indisponível ao checar bloqueio de login", e);
            return false;
        }
    }

    public void registrarFalha(String email) {
        try {
            String chaveFalhas = chaveFalhas(email);

            Long falhas = redis.opsForValue().increment(chaveFalhas);
            redis.expire(chaveFalhas, TEMPO_BLOQUEIO);

            if (falhas != null && falhas >= MAX_FALHAS) {
                redis.opsForValue().set(chaveBloqueio(email), "1", TEMPO_BLOQUEIO);
                redis.delete(chaveFalhas);
            }
        } catch (Exception e) {
            log.warn("Redis indisponível ao registrar falha de login: {}", e.getMessage());
        }
    }

    public void resetar(String email) {
        try {
            redis.delete(chaveFalhas(email));
        } catch (Exception e) {
            log.warn("Redis indisponível ao limpar falhas de login: {}", e.getMessage());
        }
    }

    private String chaveFalhas(String email) {
        return "login:fail:" + normalizar(email);
    }

    private String chaveBloqueio(String email) {
        return "login:lock:" + normalizar(email);
    }

    private String normalizar(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}