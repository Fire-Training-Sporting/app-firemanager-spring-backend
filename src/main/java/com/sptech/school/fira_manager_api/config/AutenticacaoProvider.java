package com.sptech.school.fira_manager_api.config;

import com.sptech.school.fira_manager_api.service.AutenticacaoService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AutenticacaoProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoProvider.class);
    private final AutenticacaoService autenticacaoService;
    private final PasswordEncoder passwordEncoder;

    public AutenticacaoProvider(AutenticacaoService autenticacaoService, PasswordEncoder passwordEncoder) {
        this.autenticacaoService = autenticacaoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = String.valueOf(authentication.getPrincipal());
        final String senha = String.valueOf(authentication.getCredentials());

        UserDetails userDetails = autenticacaoService.loadUserByUsername(username);

        if (!passwordEncoder.matches(senha, userDetails.getPassword())) {
            log.warn("Tentativa de login falhou para o usuário: {}", username);
            throw new BadCredentialsException("Credenciais inválidas");
        }

        log.info("Login bem-sucedido para o usuário: {}", username);
        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
