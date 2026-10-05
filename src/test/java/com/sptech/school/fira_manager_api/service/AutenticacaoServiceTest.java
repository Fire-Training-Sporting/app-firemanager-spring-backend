package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.UsuarioDetalhesDto;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Test
    void deveRetornarUsuarioDetalhesQuandoUsuarioExistir() {

        String email = "teste@email.com";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha("123456");

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(usuario));

        UserDetails resultado =
                autenticacaoService.loadUserByUsername(email);

        assertNotNull(resultado);
        assertInstanceOf(UsuarioDetalhesDto.class, resultado);
        assertEquals(email, resultado.getUsername());

        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {

        String email = "inexistente@email.com";

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception =
                assertThrows(
                        UsernameNotFoundException.class,
                        () -> autenticacaoService.loadUserByUsername(email)
                );

        assertEquals(
                "Usuário não encontrado: " + email,
                exception.getMessage()
        );

        verify(usuarioRepository).findByEmail(email);
    }
}