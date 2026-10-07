package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.responses.tipoUsuario.TipoUsuarioResponse;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.repository.TipoUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioServiceTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private TipoUsuarioService tipoUsuarioService;

    @Test
    void deveRetornarTipoUsuariosSemRoot() {
        TipoUsuario admin = new TipoUsuario();
        admin.setId(1L);
        admin.setCargo("Admin");

        TipoUsuario root = new TipoUsuario();
        root.setId(2L);
        root.setCargo("Root");

        TipoUsuario professor = new TipoUsuario();
        professor.setId(3L);
        professor.setCargo("Professor");

        when(tipoUsuarioRepository.findAll())
                .thenReturn(List.of(admin, root, professor));

        List<TipoUsuarioResponse> resultado =
                tipoUsuarioService.buscarTipoUsuarios();

        assertEquals(2, resultado.size());

        assertTrue(
                resultado.stream()
                        .noneMatch(tipo ->
                                tipo.getCargo().equalsIgnoreCase("root")
                        )
        );
    }

    @Test
    void deveRetornarListaVaziaQuandoTodosForemRoot() {
        TipoUsuario root1 = new TipoUsuario();
        root1.setId(1L);
        root1.setCargo("root");

        TipoUsuario root2 = new TipoUsuario();
        root2.setId(2L);
        root2.setCargo("ROOT");

        when(tipoUsuarioRepository.findAll())
                .thenReturn(List.of(root1, root2));

        List<TipoUsuarioResponse> resultado =
                tipoUsuarioService.buscarTipoUsuarios();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveRetornarListaVaziaQuandoRepositorioNaoPossuirDados() {
        when(tipoUsuarioRepository.findAll())
                .thenReturn(List.of());

        List<TipoUsuarioResponse> resultado =
                tipoUsuarioService.buscarTipoUsuarios();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}