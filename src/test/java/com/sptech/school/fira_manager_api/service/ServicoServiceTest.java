package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
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
class ServicoServiceTest {

    @Mock private ServicoRepository servicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private Servico criarServico(Long id, String nome, Boolean ativo) {
        Servico servico = new Servico();
        servico.setId(id);
        servico.setNome(nome);
        servico.setAtivo(ativo);
        return servico;
    }

    @Nested
    class BuscarServicos {

        @Test
        @DisplayName("Buscar Todos os Serviços com Sucesso")
        void buscarServicosComSucesso() {
            List<Servico> servicos = List.of(
                    criarServico(1L, "Tênis", true),
                    criarServico(2L, "Natação", true),
                    criarServico(3L, "Futebol", false)
            );

            when(servicoRepository.findAll()).thenReturn(servicos);

            List<ServicoResponse> response = servicoService.buscarServicos();

            assertNotNull(response);
            assertEquals(3, response.size());
            assertEquals("Tênis", response.get(0).getNome());
            assertTrue(response.get(0).getAtivo());
            assertFalse(response.get(2).getAtivo());
        }

        @Test
        @DisplayName("Buscar Serviços - Lista Vazia")
        void buscarServicosListaVazia() {
            when(servicoRepository.findAll()).thenReturn(Collections.emptyList());

            List<ServicoResponse> response = servicoService.buscarServicos();

            assertNotNull(response);
            assertTrue(response.isEmpty());
        }
    }

    @Nested
    class BuscarServicoPorId {

        @Test
        @DisplayName("Buscar Serviço por ID com Sucesso")
        void buscarServicoPorIdComSucesso() {
            Servico servico = criarServico(1L, "Tênis", true);

            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

            ServicoResponse response = servicoService.buscarServicoPorId(1L);

            assertNotNull(response);
            assertEquals(1L, response.getId());
            assertEquals("Tênis", response.getNome());
            assertTrue(response.getAtivo());
        }

        @Test
        @DisplayName("Buscar Serviço por ID Falho - Não Encontrado")
        void buscarServicoPorIdNaoEncontrado() {
            when(servicoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> servicoService.buscarServicoPorId(99L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class AtualizarAtividade {

        @Test
        @DisplayName("Ativar Serviço com Sucesso")
        void ativarServicoComSucesso() {
            Servico servicoInativo = criarServico(1L, "Tênis", false);
            Servico servicoAtivado = criarServico(1L, "Tênis", true);

            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servicoInativo));
            when(servicoRepository.save(any(Servico.class))).thenReturn(servicoAtivado);

            ServicoResponse response = servicoService.atualizarAtividadePorId(1L, 1);

            assertNotNull(response);
            assertTrue(response.getAtivo());
            assertEquals("Tênis", response.getNome());
        }

        @Test
        @DisplayName("Desativar Serviço com Sucesso")
        void desativarServicoComSucesso() {
            Servico servicoAtivo = criarServico(1L, "Natação", true);
            Servico servicoDesativado = criarServico(1L, "Natação", false);

            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servicoAtivo));
            when(servicoRepository.save(any(Servico.class))).thenReturn(servicoDesativado);

            ServicoResponse response = servicoService.atualizarAtividadePorId(1L, 0);

            assertNotNull(response);
            assertFalse(response.getAtivo());
            assertEquals("Natação", response.getNome());
        }

        @Test
        @DisplayName("Atualizar Atividade Falho - Serviço Não Encontrado")
        void atualizarAtividadeServicoNaoEncontrado() {
            when(servicoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> servicoService.atualizarAtividadePorId(99L, 1)
            );
            assertEquals(404, exception.getStatusCode().value());
            verify(servicoRepository, never()).save(any());
        }
    }

    @Nested
    class DeletarServico {

        @Test
        @DisplayName("Deletar Serviço com Sucesso")
        void deletarServicoComSucesso() {
            when(servicoRepository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> servicoService.deletarServicoPorId(1L));

            verify(servicoRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Deletar Serviço Falho - Não Encontrado")
        void deletarServicoNaoEncontrado() {
            when(servicoRepository.existsById(99L)).thenReturn(false);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> servicoService.deletarServicoPorId(99L)
            );
            assertEquals(404, exception.getStatusCode().value());
            verify(servicoRepository, never()).deleteById(any());
        }
    }
}
