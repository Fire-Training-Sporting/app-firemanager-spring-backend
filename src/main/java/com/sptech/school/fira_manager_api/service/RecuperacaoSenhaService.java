package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.client.NotificationServiceClient;
import com.sptech.school.fira_manager_api.dto.requests.notificationService.EmailRecuperacaoSenhaNotification;
import com.sptech.school.fira_manager_api.model.TokenRecuperacaoSenha;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.TokenRecuperacaoSenhaRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class RecuperacaoSenhaService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Duration TEMPO_EXPIRACAO_PADRAO = Duration.ofMinutes(30);
    private static final Duration INTERVALO_SOLICITACAO = Duration.ofMinutes(1);

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacaoSenhaRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationServiceClient notificationServiceClient;
    private final String urlRedefinicao;

    public RecuperacaoSenhaService(
            UsuarioRepository usuarioRepository,
            TokenRecuperacaoSenhaRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            NotificationServiceClient notificationServiceClient,
            @Value("${app.password-reset.url:http://localhost:3000/reset-password}") String urlRedefinicao) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationServiceClient = notificationServiceClient;
        this.urlRedefinicao = urlRedefinicao;
    }

    @Transactional
    public void solicitarRecuperacao(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email.trim())
                .orElse(null);
        if (usuario == null) {
            return;
        }

        Instant agora = Instant.now();
        boolean emIntervaloDeEspera = tokenRepository
                .findFirstByUsuario_IdOrderByCriadoEmDesc(usuario.getId())
                .map(token -> token.getCriadoEm().plus(INTERVALO_SOLICITACAO).isAfter(agora))
                .orElse(false);
        if (emIntervaloDeEspera) {
            return;
        }

        String tokenPlano = gerarToken();
        tokenRepository.invalidarTokensAtivosDoUsuario(usuario.getId());

        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha();
        token.setUsuario(usuario);
        token.setTokenHash(hashToken(tokenPlano));
        token.setCriadoEm(agora);
        token.setExpiraEm(agora.plus(TEMPO_EXPIRACAO_PADRAO));
        token.setUtilizado(false);
        tokenRepository.save(token);

        publicarNotificacao(usuario.getEmail(), tokenPlano);
    }

    @Transactional
    public void redefinirSenha(String tokenPlano, String novaSenha) {
        TokenRecuperacaoSenha token = tokenRepository.findByTokenHashForUpdate(hashToken(tokenPlano))
                .filter(registro -> !registro.isUtilizado() && registro.getExpiraEm().isAfter(Instant.now()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Token inválido ou expirado"));

        Usuario usuario = token.getUsuario();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        token.setUtilizado(true);
        tokenRepository.save(token);
    }

    private void publicarNotificacao(String email, String token) {
        String link = UriComponentsBuilder.fromUriString(urlRedefinicao)
                .queryParam("token", token)
                .build()
                .encode()
                .toUriString();
        notificationServiceClient.notificarRecuperacaoSenha(
                new EmailRecuperacaoSenhaNotification(email, link, (int) TEMPO_EXPIRACAO_PADRAO.toMinutes()));
    }

    private String gerarToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 indisponível", ex);
        }
    }
}
