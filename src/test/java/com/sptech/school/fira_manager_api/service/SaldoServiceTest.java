package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.requests.saldo.SaldoRequest;
import com.sptech.school.fira_manager_api.dto.responses.saldo.ProfessorSaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.saldo.SaldoResponse;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.AgendamentoRepository;
import com.sptech.school.fira_manager_api.repository.SaldoRepository;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaldoServiceTest {

    @Mock private SaldoRepository saldoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ServicoRepository servicoRepository;
    @Mock private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private SaldoService saldoService;

    @Nested
    class CriarSaldo {

        @Test
        @DisplayName("Criar Saldo com Sucesso")
        void criarSaldoComSucesso() {
            SaldoRequest request = new SaldoRequest();
            request.setAluno(1L);
            request.setServico(1L);
            request.setQuantidade(10.0);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario aluno = new Usuario();
            aluno.setId(1L);
            aluno.setNome("João Silva");
            aluno.setEmail("joao@gmail.com");
            aluno.setTelefone("11912345678");
            aluno.setTipoUsuario(tipo);

            Servico servico = new Servico();
            servico.setId(1L);
            servico.setNome("Tênis");

            Saldo saldoSalvo = new Saldo();
            saldoSalvo.setId(1L);
            saldoSalvo.setAluno(aluno);
            saldoSalvo.setServico(servico);
            saldoSalvo.setQuantidade(10.0);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            when(saldoRepository.save(any(Saldo.class))).thenReturn(saldoSalvo);

            SaldoResponse response = saldoService.criarSaldo(request);

            assertNotNull(response);
            assertEquals(10.0, response.getQuantidade());
            assertEquals("Tênis", response.getServico().getNome());
        }

        @Test
        @DisplayName("Criar Saldo Falho - Aluno não encontrado")
        void criarSaldoAlunoNaoEncontrado() {
            SaldoRequest request = new SaldoRequest();
            request.setAluno(99L);
            request.setServico(1L);
            request.setQuantidade(10.0);

            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.criarSaldo(request)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Criar Saldo Falho - Serviço não encontrado")
        void criarSaldoServicoNaoEncontrado() {
            SaldoRequest request = new SaldoRequest();
            request.setAluno(1L);
            request.setServico(99L);
            request.setQuantidade(10.0);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario aluno = new Usuario();
            aluno.setId(1L);
            aluno.setNome("Pietrão Phineas");
            aluno.setEmail("Pietrao@gmail.com");
            aluno.setTelefone("11912345678");
            aluno.setTipoUsuario(tipo);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(servicoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.criarSaldo(request)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class ListarSaldos {

        @Test
        @DisplayName("Listar Todos os Saldos")
        void listarTodosSaldos() {
            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario aluno = new Usuario();
            aluno.setId(1L);
            aluno.setNome("João Silva");
            aluno.setEmail("joao@gmail.com");
            aluno.setTelefone("11912345678");
            aluno.setTipoUsuario(tipo);

            Servico servico = new Servico();
            servico.setId(1L);
            servico.setNome("Tênis");

            Saldo saldo1 = new Saldo();
            saldo1.setId(1L);
            saldo1.setAluno(aluno);
            saldo1.setServico(servico);
            saldo1.setQuantidade(10.0);

            Saldo saldo2 = new Saldo();
            saldo2.setId(2L);
            saldo2.setAluno(aluno);
            saldo2.setServico(servico);
            saldo2.setQuantidade(5.0);

            when(saldoRepository.findAll()).thenReturn(List.of(saldo1, saldo2));

            List<SaldoResponse> response = saldoService.listarSaldos();

            assertNotNull(response);
            assertEquals(2, response.size());
        }

        @Test
        @DisplayName("Listar Saldo por ID com Sucesso")
        void listarSaldoPorId() {
            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario aluno = new Usuario();
            aluno.setId(1L);
            aluno.setNome("João Silva");
            aluno.setEmail("joao@gmail.com");
            aluno.setTelefone("11912345678");
            aluno.setTipoUsuario(tipo);

            Servico servico = new Servico();
            servico.setId(1L);
            servico.setNome("Tênis");

            Saldo saldo = new Saldo();
            saldo.setId(1L);
            saldo.setAluno(aluno);
            saldo.setServico(servico);
            saldo.setQuantidade(10.0);

            when(saldoRepository.findById(1L)).thenReturn(Optional.of(saldo));

            SaldoResponse response = saldoService.listarSaldoPorId(1L);

            assertNotNull(response);
            assertEquals(10.0, response.getQuantidade());
            assertEquals("João Silva", response.getAluno().getNome());
        }

        @Test
        @DisplayName("Listar Saldo por ID Falho - Não encontrado")
        void listarSaldoPorIdNaoEncontrado() {
            when(saldoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.listarSaldoPorId(99L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class AtualizarSaldo {

        @Test
        @DisplayName("Atualizar Saldo com Sucesso")
        void atualizarSaldoComSucesso() {
            SaldoRequest request = new SaldoRequest();
            request.setAluno(1L);
            request.setServico(1L);
            request.setQuantidade(20.0);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario aluno = new Usuario();
            aluno.setId(1L);
            aluno.setNome("João Silva");
            aluno.setEmail("joao@gmail.com");
            aluno.setTelefone("11912345678");
            aluno.setTipoUsuario(tipo);

            Servico servico = new Servico();
            servico.setId(1L);
            servico.setNome("Tênis");

            Saldo saldoExistente = new Saldo();
            saldoExistente.setId(1L);
            saldoExistente.setAluno(aluno);
            saldoExistente.setServico(servico);
            saldoExistente.setQuantidade(10.0);

            Saldo saldoAtualizado = new Saldo();
            saldoAtualizado.setId(1L);
            saldoAtualizado.setAluno(aluno);
            saldoAtualizado.setServico(servico);
            saldoAtualizado.setQuantidade(20.0);

            when(saldoRepository.findById(1L)).thenReturn(Optional.of(saldoExistente));
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            when(saldoRepository.save(any(Saldo.class))).thenReturn(saldoAtualizado);

            SaldoResponse response = saldoService.atualizarSaldoPorId(request, 1L);

            assertNotNull(response);
            assertEquals(20.0, response.getQuantidade());
        }

        @Test
        @DisplayName("Atualizar Saldo Falho - Saldo não encontrado")
        void atualizarSaldoNaoEncontrado() {
            SaldoRequest request = new SaldoRequest();
            request.setAluno(1L);
            request.setServico(1L);
            request.setQuantidade(20.0);

            when(saldoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.atualizarSaldoPorId(request, 99L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Saldo Falho - Aluno não encontrado")
        void atualizarSaldoAlunoNaoEncontrado() {
            SaldoRequest request = new SaldoRequest();
            request.setAluno(99L);
            request.setServico(1L);
            request.setQuantidade(20.0);

            Saldo saldoExistente = new Saldo();
            saldoExistente.setId(1L);

            when(saldoRepository.findById(1L)).thenReturn(Optional.of(saldoExistente));
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.atualizarSaldoPorId(request, 1L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Saldo Falho - Serviço não encontrado")
        void atualizarSaldoServicoNaoEncontrado() {
            SaldoRequest request = new SaldoRequest();
            request.setAluno(1L);
            request.setServico(99L);
            request.setQuantidade(20.0);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario aluno = new Usuario();
            aluno.setId(1L);
            aluno.setNome("João Silva");
            aluno.setEmail("joao@gmail.com");
            aluno.setTelefone("11912345678");
            aluno.setTipoUsuario(tipo);

            Saldo saldoExistente = new Saldo();
            saldoExistente.setId(1L);

            when(saldoRepository.findById(1L)).thenReturn(Optional.of(saldoExistente));
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(servicoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.atualizarSaldoPorId(request, 1L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class DeletarSaldo {

        @Test
        @DisplayName("Deletar Saldo com Sucesso")
        void deletarSaldo() {
            when(saldoRepository.existsById(1L)).thenReturn(true);

            saldoService.deletarSaldoPorId(1L);
            verify(saldoRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deletar Saldo Falho - Não encontrado")
        void deletarSaldoNaoEncontrado() {
            when(saldoRepository.existsById(99L)).thenReturn(false);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.deletarSaldoPorId(99L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class BuscarSaldoProfessor {

        @Test
        @DisplayName("Buscar Saldo Professor com Sucesso")
        void buscarSaldoProfessorComSucesso() {
            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(3L);
            tipo.setCargo("professor");

            Usuario professor = new Usuario();
            professor.setId(1L);
            professor.setNome("Luan Mãe solteira");
            professor.setEmail("luanBucchi@gmail.com");
            professor.setTelefone("11988887777");
            professor.setTipoUsuario(tipo);

            Servico servico = new Servico();
            servico.setId(1L);
            servico.setNome("tênis");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(professor));
            when(servicoRepository.findAll()).thenReturn(List.of(servico));
            when(agendamentoRepository.countByProfessorIdAndServicoIdAndStatus(1L, 1L, "finalizado")).thenReturn(3L);
            when(agendamentoRepository.countByAuxiliarIdAndServicoIdAndStatus(1L, 1L, "finalizado")).thenReturn(1L);

            ProfessorSaldoResponse response = saldoService.buscarSaldoProfessorPorId(1L);

            assertNotNull(response);
            assertEquals("Luan Souza", response.getProfessor().getNome());
            assertEquals(1, response.getServicos().size());
            assertEquals(3, response.getAulasComoProfessor());
            assertEquals(1, response.getAulasComoAuxiliar());
            assertEquals(4, response.getTotalAulas());
        }

        @Test
        @DisplayName("Buscar Saldo Professor Falho - Usuário não encontrado")
        void buscarSaldoProfessorNaoEncontrado() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.buscarSaldoProfessorPorId(99L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Buscar Saldo Professor Falho - Usuário não é professor")
        void buscarSaldoProfessorUsuarioNaoEProfessor() {
            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario aluno = new Usuario();
            aluno.setId(1L);
            aluno.setNome("João Silva");
            aluno.setEmail("joao@gmail.com");
            aluno.setTelefone("11912345678");
            aluno.setTipoUsuario(tipo);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> saldoService.buscarSaldoProfessorPorId(1L)
            );
            assertEquals(400, exception.getStatusCode().value());
        }
    }
}
