package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.requests.permissao.PermissaoRequest;
import com.sptech.school.fira_manager_api.dto.responses.permissao.PermissaoResponse;
import com.sptech.school.fira_manager_api.model.Permissao;
import com.sptech.school.fira_manager_api.repository.PermissaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissaoServiceTest {

    @Mock private PermissaoRepository permissaoRepository;

    @InjectMocks
    private PermissaoService permissaoService;

    private Permissao criarPermissao(Long id, String nome) {
        Permissao permissao = new Permissao();
        permissao.setId(id);
        permissao.setNome(nome);
        return permissao;
    }

    private PermissaoRequest criarRequest(String nome) {
        PermissaoRequest request = new PermissaoRequest();
        request.setNome(nome);
        return request;
    }

    @Nested
    class AdicionarPermissao {

        @Test
        @DisplayName("Adicionar Permissão com Sucesso")
        void adicionarPermissaoComSucesso() {
            PermissaoRequest request = criarRequest("Criar agendamento");
            Permissao salva = criarPermissao(1L, "Criar agendamento");

            when(permissaoRepository.save(any(Permissao.class))).thenReturn(salva);

            PermissaoResponse response = permissaoService.adicionarNovaPermissao(request);

            assertNotNull(response);
            assertEquals(1L, response.getId());
            assertEquals("Criar agendamento", response.getNome());
            verify(permissaoRepository, times(1)).save(any(Permissao.class));
        }
    }

    @Nested
    class ObterPermissoes {

        @Test
        @DisplayName("Obter Todas as Permissões com Sucesso")
        void obterPermissoesComSucesso() {
            List<Permissao> permissoes = List.of(
                    criarPermissao(1L, "Criar agendamento"),
                    criarPermissao(2L, "Cancelar agendamento"),
                    criarPermissao(3L, "Gerenciar usuários")
            );

            when(permissaoRepository.findAll()).thenReturn(permissoes);

            List<PermissaoResponse> response = permissaoService.obterPermissoes();

            assertNotNull(response);
            assertEquals(3, response.size());
            assertEquals("Criar agendamento", response.get(0).getNome());
            assertEquals("Cancelar agendamento", response.get(1).getNome());
        }

        @Test
        @DisplayName("Obter Permissões - Lista Vazia")
        void obterPermissoesListaVazia() {
            when(permissaoRepository.findAll()).thenReturn(Collections.emptyList());

            List<PermissaoResponse> response = permissaoService.obterPermissoes();

            assertNotNull(response);
            assertTrue(response.isEmpty());
        }
    }

    @Nested
    class AtualizarPermissao {

        @Test
        @DisplayName("Atualizar Permissão com Sucesso")
        void atualizarPermissaoComSucesso() {
            Permissao existente = criarPermissao(1L, "Criar agendamento");
            Permissao atualizada = criarPermissao(1L, "Criar ou editar agendamento");
            PermissaoRequest request = criarRequest("Criar ou editar agendamento");

            when(permissaoRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(permissaoRepository.save(any(Permissao.class))).thenReturn(atualizada);

            PermissaoResponse response = permissaoService.atualizarPermissao(1L, request);

            assertNotNull(response);
            assertEquals("Criar ou editar agendamento", response.getNome());
            assertEquals(1L, response.getId());
        }

        @Test
        @DisplayName("Atualizar Permissão Falho - Não Encontrada")
        void atualizarPermissaoNaoEncontrada() {
            PermissaoRequest request = criarRequest("Criar agendamento");

            when(permissaoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> permissaoService.atualizarPermissao(99L, request)
            );
            assertEquals(404, exception.getStatusCode().value());
            verify(permissaoRepository, never()).save(any());
        }
    }

    @Nested
    class DeletarPermissao {

        @Test
        @DisplayName("Deletar Permissão com Sucesso")
        void deletarPermissaoComSucesso() {
            when(permissaoRepository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> permissaoService.deletarPermissao(1L));

            verify(permissaoRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Deletar Permissão Falho - Não Encontrada")
        void deletarPermissaoNaoEncontrada() {
            when(permissaoRepository.existsById(99L)).thenReturn(false);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> permissaoService.deletarPermissao(99L)
            );
            assertEquals(404, exception.getStatusCode().value());
            verify(permissaoRepository, never()).deleteById(any());
        }
    }
}
