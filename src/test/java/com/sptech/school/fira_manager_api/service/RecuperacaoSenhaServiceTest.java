package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.client.NotificationServiceClient;
import com.sptech.school.fira_manager_api.dto.requests.notificationService.EmailRecuperacaoSenhaNotification;
import com.sptech.school.fira_manager_api.model.TokenRecuperacaoSenha;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.TokenRecuperacaoSenhaRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecuperacaoSenhaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TokenRecuperacaoSenhaRepository tokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NotificationServiceClient notificationServiceClient;

    private RecuperacaoSenhaService service;

    @BeforeEach
    void setUp() {
        service = new RecuperacaoSenhaService(
                usuarioRepository, tokenRepository, passwordEncoder, notificationServiceClient,
                "https://app.example.com/reset-password");
    }

    @Test
    void deveCriarTokenHashEPublicarNotificacaoParaContaExistente() {
        Usuario usuario = usuario();
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(tokenRepository.findFirstByUsuario_IdOrderByCriadoEmDesc(usuario.getId()))
                .thenReturn(Optional.empty());

        service.solicitarRecuperacao(usuario.getEmail());

        ArgumentCaptor<TokenRecuperacaoSenha> tokenCaptor =
                ArgumentCaptor.forClass(TokenRecuperacaoSenha.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        TokenRecuperacaoSenha salvo = tokenCaptor.getValue();
        assertNotNull(salvo.getTokenHash());
        assertEquals(64, salvo.getTokenHash().length());
        assertFalse(salvo.isUtilizado());
        assertTrue(salvo.getExpiraEm().isAfter(Instant.now()));

        ArgumentCaptor<EmailRecuperacaoSenhaNotification> notificacaoCaptor =
                ArgumentCaptor.forClass(EmailRecuperacaoSenhaNotification.class);
        verify(notificationServiceClient).notificarRecuperacaoSenha(notificacaoCaptor.capture());
        EmailRecuperacaoSenhaNotification notificacao = notificacaoCaptor.getValue();
        assertEquals(usuario.getEmail(), notificacao.emailDestinatario());
        assertTrue(notificacao.linkRedefinicao()
                .matches("https://app\\.example\\.com/reset-password\\?token=[A-Za-z0-9_-]{43}"));
        assertEquals(30, notificacao.validadeMinutos());
        assertNotEquals(
                notificacao.linkRedefinicao().substring(notificacao.linkRedefinicao().indexOf("?token=") + 7),
                salvo.getTokenHash());
    }

    @Test
    void naoDeveRevelarSeEmailNaoExisteNemPublicarMensagem() {
        when(usuarioRepository.findByEmail("nao-existe@example.com")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> service.solicitarRecuperacao("nao-existe@example.com"));

        verifyNoInteractions(tokenRepository, notificationServiceClient);
    }

    @Test
    void deveAplicarIntervaloEntreSolicitacoesDaMesmaConta() {
        Usuario usuario = usuario();
        TokenRecuperacaoSenha tokenAnterior = new TokenRecuperacaoSenha();
        tokenAnterior.setCriadoEm(Instant.now());
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(tokenRepository.findFirstByUsuario_IdOrderByCriadoEmDesc(usuario.getId()))
                .thenReturn(Optional.of(tokenAnterior));

        service.solicitarRecuperacao(usuario.getEmail());

        verify(tokenRepository, never()).save(any());
        verifyNoInteractions(notificationServiceClient);
    }

    @Test
    void deveAtualizarSenhaEInvalidarTokenAoRedefinir() {
        TokenRecuperacaoSenha token = tokenValido();
        when(tokenRepository.findByTokenHashForUpdate(any())).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("nova123")).thenReturn("hash-bcrypt");

        service.redefinirSenha("token-valido", "nova123");

        assertEquals("hash-bcrypt", token.getUsuario().getSenha());
        assertTrue(token.isUtilizado());
        verify(usuarioRepository).save(token.getUsuario());
        verify(tokenRepository).save(token);
    }

    @Test
    void deveRejeitarTokenDesconhecido() {
        when(tokenRepository.findByTokenHashForUpdate(any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.redefinirSenha("token-invalido", "nova123"));

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void deveRejeitarTokenExpirado() {
        TokenRecuperacaoSenha token = tokenValido();
        token.setExpiraEm(Instant.now().minusSeconds(1));
        when(tokenRepository.findByTokenHashForUpdate(any())).thenReturn(Optional.of(token));

        assertThrows(ResponseStatusException.class,
                () -> service.redefinirSenha("token-expirado", "nova123"));

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void deveRejeitarTokenJaUtilizado() {
        TokenRecuperacaoSenha token = tokenValido();
        token.setUtilizado(true);
        when(tokenRepository.findByTokenHashForUpdate(any())).thenReturn(Optional.of(token));

        assertThrows(ResponseStatusException.class,
                () -> service.redefinirSenha("token-reutilizado", "nova123"));

        verifyNoInteractions(passwordEncoder);
    }

    private Usuario usuario() {
        Usuario usuario = new Usuario();
        usuario.setId(42L);
        usuario.setEmail("user@example.com");
        return usuario;
    }

    private TokenRecuperacaoSenha tokenValido() {
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha();
        token.setUsuario(usuario());
        token.setExpiraEm(Instant.now().plusSeconds(600));
        token.setUtilizado(false);
        return token;
    }
}
