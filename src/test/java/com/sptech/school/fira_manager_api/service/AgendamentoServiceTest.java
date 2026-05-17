package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoRecorrenteRequest;
import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoRequest;
import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoStatusRequest;
import com.sptech.school.fira_manager_api.dto.responses.agendamento.AgendamentoResponse;
import com.sptech.school.fira_manager_api.model.*;
import com.sptech.school.fira_manager_api.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private CondominioRepository condominioRepository;
    @Mock private ServicoRepository servicoRepository;
    @Mock private SaldoRepository saldoRepository;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Usuario criarAluno() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(4L);
        tipo.setCargo("aluno");
        Usuario aluno = new Usuario();
        aluno.setId(1L);
        aluno.setNome("Pietro e Seus segredos");
        aluno.setEmail("Pietro@gmail.com");
        aluno.setTelefone("11912345678");
        aluno.setTipoUsuario(tipo);
        return aluno;
    }

    private Usuario criarProfessor() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(3L);
        tipo.setCargo("professor");
        Usuario professor = new Usuario();
        professor.setId(2L);
        professor.setNome("Luan Ama mães solteiras");
        professor.setEmail("luanBucchi@gmail.com");
        professor.setTelefone("11988887777");
        professor.setTipoUsuario(tipo);
        return professor;
    }

    private Servico criarServico() {
        Servico servico = new Servico();
        servico.setId(1L);
        servico.setNome("Tênis");
        servico.setAtivo(true);
        return servico;
    }

    private Condominio criarCondominio() {
        Condominio condominio = new Condominio();
        condominio.setId(1L);
        condominio.setNome("Condomínio que o luan foi convidado");
        condominio.setCidade("São Paulo");
        condominio.setBairro("Jardim Paulista");
        return condominio;
    }

    private Saldo criarSaldo(Usuario aluno, Servico servico, Double quantidade) {
        Saldo saldo = new Saldo();
        saldo.setId(1L);
        saldo.setAluno(aluno);
        saldo.setServico(servico);
        saldo.setQuantidade(quantidade);
        return saldo;
    }

    private Agendamento criarAgendamentoSalvo(Usuario aluno, Usuario professor, Servico servico, Condominio condominio) {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setAluno(aluno);
        agendamento.setProfessor(professor);
        agendamento.setServico(servico);
        agendamento.setCondominio(condominio);
        agendamento.setData(LocalDate.now().plusDays(2));
        agendamento.setHoraInicio(LocalTime.of(10, 0));
        agendamento.setHoraFim(LocalTime.of(11, 0));
        agendamento.setStatus("pendente");
        return agendamento;
    }

    private AgendamentoRequest criarRequest() {
        AgendamentoRequest request = new AgendamentoRequest();
        request.setAluno(1L);
        request.setProfessor(2L);
        request.setServico(1L);
        request.setCondominio(1L);
        request.setData(LocalDate.now().plusDays(2));
        request.setHoraInicio(LocalTime.of(10, 0));
        request.setHoraFim(LocalTime.of(11, 0));
        return request;
    }

    private void mocksCriacaoBase(Usuario aluno, Usuario professor, Servico servico, Condominio condominio, Saldo saldo) {
        when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
        when(agendamentoRepository.findByProfessorIdAndDataAndStatusNot(eq(2L), any(LocalDate.class), eq("cancelado")))
                .thenReturn(List.of());
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(professor));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
        when(saldoRepository.save(any(Saldo.class))).thenReturn(saldo);
    }

    @Nested
    class CriarAgendamento {

        @Test
        @DisplayName("Criar Agendamento com Sucesso")
        void criarAgendamentoComSucesso() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamentoSalvo = criarAgendamentoSalvo(aluno, professor, servico, condominio);

            mocksCriacaoBase(aluno, professor, servico, condominio, saldo);
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoSalvo);

            AgendamentoResponse response = agendamentoService.criarAgendamento(criarRequest());

            assertNotNull(response);
            assertEquals("Pietro e Seus segredos", response.getAluno().getNome());
            assertEquals("Luan Ama mães solteiras", response.getProfessor().getNome());
            assertEquals("pendente", response.getStatus());
        }

        @Test
        @DisplayName("Criar Agendamento com Auxiliar com Sucesso")
        void criarAgendamentoComAuxiliarComSucesso() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Usuario auxiliar = new Usuario();
            auxiliar.setId(3L);
            auxiliar.setNome("Carlos Mendes");
            auxiliar.setEmail("carlos@gmail.com");
            auxiliar.setTelefone("11977776666");
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);

            Agendamento agendamentoSalvo = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamentoSalvo.setAuxiliar(auxiliar);

            AgendamentoRequest request = criarRequest();
            request.setAuxiliar(3L);

            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
            when(agendamentoRepository.findByProfessorIdAndDataAndStatusNot(eq(2L), any(LocalDate.class), eq("cancelado")))
                    .thenReturn(List.of());
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(usuarioRepository.findById(2L)).thenReturn(Optional.of(professor));
            when(usuarioRepository.findById(3L)).thenReturn(Optional.of(auxiliar));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
            when(saldoRepository.save(any(Saldo.class))).thenReturn(saldo);
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoSalvo);

            AgendamentoResponse response = agendamentoService.criarAgendamento(request);

            assertNotNull(response);
            assertNotNull(response.getAuxiliar());
            assertEquals("Carlos Mendes", response.getAuxiliar().getNome());
        }

        @Test
        @DisplayName("Criar Agendamento Falho - Saldo não encontrado")
        void criarAgendamentoSaldoNaoEncontrado() {
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.criarAgendamento(criarRequest())
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Criar Agendamento Falho - Saldo insuficiente")
        void criarAgendamentoSaldoInsuficiente() {
            Usuario aluno = criarAluno();
            Servico servico = criarServico();
            Saldo saldo = criarSaldo(aluno, servico, 0.5);

            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.criarAgendamento(criarRequest())
            );
            assertEquals(400, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Criar Agendamento Falho - Hora fim antes da hora início")
        void criarAgendamentoHoraFimAntesHoraInicio() {
            Usuario aluno = criarAluno();
            Servico servico = criarServico();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);

            AgendamentoRequest request = criarRequest();
            request.setHoraInicio(LocalTime.of(11, 0));
            request.setHoraFim(LocalTime.of(10, 0)); // fim antes do início

            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.criarAgendamento(request)
            );
            assertEquals(400, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Criar Agendamento Falho - Intervalo não é múltiplo de 30 minutos")
        void criarAgendamentoIntervaloNaoMultiploDe30() {
            Usuario aluno = criarAluno();
            Servico servico = criarServico();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);

            AgendamentoRequest request = criarRequest();
            request.setHoraInicio(LocalTime.of(10, 0));
            request.setHoraFim(LocalTime.of(10, 45)); // 45 min — não é múltiplo de 30

            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.criarAgendamento(request)
            );
            assertEquals(400, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Criar Agendamento Falho - Professor precisa de 1h de intervalo entre condomínios diferentes")
        void criarAgendamentoConflitoIntervaloEntreCondominios() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);

            Condominio condominioOutro = new Condominio();
            condominioOutro.setId(2L);
            condominioOutro.setNome("Condomínio Outro");

            // Agendamento existente no condomínio 2: 12:00 - 13:00
            Agendamento agendamentoExistente = criarAgendamentoSalvo(aluno, professor, servico, condominioOutro);
            agendamentoExistente.setHoraInicio(LocalTime.of(12, 0));
            agendamentoExistente.setHoraFim(LocalTime.of(13, 0));

            // Novo agendamento no condomínio 1: 13:30 - 14:30 (menos de 1h de intervalo)
            AgendamentoRequest request = criarRequest();
            request.setHoraInicio(LocalTime.of(13, 30));
            request.setHoraFim(LocalTime.of(14, 30));

            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
            when(agendamentoRepository.findByProfessorIdAndDataAndStatusNot(eq(2L), any(LocalDate.class), eq("cancelado")))
                    .thenReturn(List.of(agendamentoExistente));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.criarAgendamento(request)
            );
            assertEquals(409, exception.getStatusCode().value());
        }
    }

    @Nested
    class CriarAgendamentoRecorrente {

        @Test
        @DisplayName("Criar Agendamento Recorrente com Sucesso")
        void criarAgendamentoRecorrenteComSucesso() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 10.0);

            Agendamento agendamentoSalvo = criarAgendamentoSalvo(aluno, professor, servico, condominio);

            AgendamentoRecorrenteRequest request = new AgendamentoRecorrenteRequest();
            request.setAluno(1L);
            request.setProfessor(2L);
            request.setServico(1L);
            request.setCondominio(1L);
            request.setData(LocalDate.now().plusDays(2));
            request.setHoraInicio(LocalTime.of(10, 0));
            request.setHoraFim(LocalTime.of(11, 0));
            request.setQuantidadeRecorrencias(3);

            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
            when(agendamentoRepository.findByProfessorIdAndDataAndStatusNot(eq(2L), any(LocalDate.class), eq("cancelado")))
                    .thenReturn(List.of());
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(usuarioRepository.findById(2L)).thenReturn(Optional.of(professor));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
            when(saldoRepository.save(any(Saldo.class))).thenReturn(saldo);
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoSalvo);

            List<AgendamentoResponse> response = agendamentoService.criarAgendamentoRecorrente(request);

            assertNotNull(response);
            assertEquals(3, response.size());
        }

        @Test
        @DisplayName("Criar Agendamento Recorrente Falho - Saldo insuficiente para a primeira aula")
        void criarAgendamentoRecorrenteSaldoInsuficiente() {
            Usuario aluno = criarAluno();
            Servico servico = criarServico();
            Saldo saldo = criarSaldo(aluno, servico, 0.5);

            AgendamentoRecorrenteRequest request = new AgendamentoRecorrenteRequest();
            request.setAluno(1L);
            request.setServico(1L);
            request.setData(LocalDate.now().plusDays(2));
            request.setHoraInicio(LocalTime.of(10, 0));
            request.setHoraFim(LocalTime.of(11, 0));
            request.setQuantidadeRecorrencias(2);

            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.criarAgendamentoRecorrente(request)
            );
            assertEquals(400, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Criar Agendamento Recorrente Falho - Recorrências excedem o saldo")
        void criarAgendamentoRecorrenteExcedeSaldo() {
            Usuario aluno = criarAluno();
            Servico servico = criarServico();
            Saldo saldo = criarSaldo(aluno, servico, 2.0);

            AgendamentoRecorrenteRequest request = new AgendamentoRecorrenteRequest();
            request.setAluno(1L);
            request.setServico(1L);
            request.setData(LocalDate.now().plusDays(2));
            request.setHoraInicio(LocalTime.of(10, 0));
            request.setHoraFim(LocalTime.of(11, 0));
            request.setQuantidadeRecorrencias(5);

            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.criarAgendamentoRecorrente(request)
            );
            assertEquals(400, exception.getStatusCode().value());
        }
    }

    @Nested
    class ListarAgendamento {

        @Test
        @DisplayName("Listar Todos os Agendamentos")
        void listarTodosAgendamentos() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);

            Agendamento ag1 = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            Agendamento ag2 = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            ag2.setId(2L);

            when(agendamentoRepository.findAll()).thenReturn(List.of(ag1, ag2));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            List<AgendamentoResponse> response = agendamentoService.listarAgendamento();

            assertNotNull(response);
            assertEquals(2, response.size());
        }

        @Test
        @DisplayName("Listar Agendamento por ID com Sucesso")
        void listarAgendamentoPorId() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            AgendamentoResponse response = agendamentoService.listarAgendamentoPorId(1L);

            assertNotNull(response);
            assertEquals("Pietro e Seus segredos", response.getAluno().getNome());
        }

        @Test
        @DisplayName("Listar Agendamento por ID Falho - Não encontrado")
        void listarAgendamentoPorIdNaoEncontrado() {
            when(agendamentoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.listarAgendamentoPorId(99L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class AtualizarAgendamento {

        @Test
        @DisplayName("Atualizar Agendamento com Sucesso - Mesmo serviço")
        void atualizarAgendamentoComSucesso() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamentoExistente = criarAgendamentoSalvo(aluno, professor, servico, condominio);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamentoExistente));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(usuarioRepository.findById(2L)).thenReturn(Optional.of(professor));
            when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoExistente);

            AgendamentoResponse response = agendamentoService.atualizarAgendamentoPorId(criarRequest(), 1L);

            assertNotNull(response);
            assertEquals("Pietro e Seus segredos", response.getAluno().getNome());
        }

        @Test
        @DisplayName("Atualizar Agendamento Falho - Agendamento não está pendente")
        void atualizarAgendamentoNaoPendente() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setStatus("confirmado");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.atualizarAgendamentoPorId(criarRequest(), 1L)
            );
            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Agendamento Falho - Agendamento não encontrado")
        void atualizarAgendamentoNaoEncontrado() {
            when(agendamentoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.atualizarAgendamentoPorId(criarRequest(), 99L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Agendamento com Sucesso - Troca de serviço")
        void atualizarAgendamentoTrocaDeServico() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servicoAntigo = criarServico(); // id=1
            Servico servicoNovo = new Servico();
            servicoNovo.setId(2L);
            servicoNovo.setNome("Natação");
            servicoNovo.setAtivo(true);
            Condominio condominio = criarCondominio();

            Agendamento agendamentoExistente = criarAgendamentoSalvo(aluno, professor, servicoAntigo, condominio);
            Saldo saldoAntigo = criarSaldo(aluno, servicoAntigo, 4.0);
            Saldo saldoNovo = criarSaldo(aluno, servicoNovo, 5.0); // suficiente

            AgendamentoRequest request = criarRequest();
            request.setServico(2L);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamentoExistente));
            when(servicoRepository.findById(2L)).thenReturn(Optional.of(servicoNovo));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldoAntigo));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 2L)).thenReturn(Optional.of(saldoNovo));
            when(saldoRepository.save(any(Saldo.class))).thenReturn(saldoNovo);
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(usuarioRepository.findById(2L)).thenReturn(Optional.of(professor));
            when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoExistente);

            AgendamentoResponse response = agendamentoService.atualizarAgendamentoPorId(request, 1L);

            assertNotNull(response);
            verify(saldoRepository, times(2)).save(any(Saldo.class)); // saldo antigo estornado + saldo novo debitado
        }

        @Test
        @DisplayName("Atualizar Agendamento Falho - Saldo insuficiente para novo serviço")
        void atualizarAgendamentoSaldoInsuficienteNovoServico() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servicoAntigo = criarServico();
            Servico servicoNovo = new Servico();
            servicoNovo.setId(2L);
            servicoNovo.setNome("Natação");
            servicoNovo.setAtivo(true);
            Condominio condominio = criarCondominio();

            Agendamento agendamentoExistente = criarAgendamentoSalvo(aluno, professor, servicoAntigo, condominio);
            Saldo saldoAntigo = criarSaldo(aluno, servicoAntigo, 5.0);
            Saldo saldoNovo = criarSaldo(aluno, servicoNovo, 0.5);

            AgendamentoRequest request = criarRequest();
            request.setServico(2L);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamentoExistente));
            when(servicoRepository.findById(2L)).thenReturn(Optional.of(servicoNovo));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldoAntigo));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 2L)).thenReturn(Optional.of(saldoNovo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.atualizarAgendamentoPorId(request, 1L)
            );
            assertEquals(400, exception.getStatusCode().value());
        }
    }

    @Nested
    class AtualizarStatusAgendamento {

        @Test
        @DisplayName("Atualizar Status - Pendente para Confirmado com Sucesso")
        void atualizarStatusPendenteParaConfirmado() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setStatus("pendente");

            AgendamentoStatusRequest statusRequest = new AgendamentoStatusRequest();
            statusRequest.setStatus("confirmado");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

            AgendamentoResponse response = agendamentoService.atualizarStatusAgendamentoPorId(1L, statusRequest);

            assertNotNull(response);
            assertEquals("confirmado", response.getStatus());
        }

        @Test
        @DisplayName("Atualizar Status - Confirmado para Finalizado com Sucesso")
        void atualizarStatusConfirmadoParaFinalizado() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setStatus("confirmado");

            AgendamentoStatusRequest statusRequest = new AgendamentoStatusRequest();
            statusRequest.setStatus("finalizado");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

            AgendamentoResponse response = agendamentoService.atualizarStatusAgendamentoPorId(1L, statusRequest);

            assertNotNull(response);
            assertEquals("finalizado", response.getStatus());
        }

        @Test
        @DisplayName("Atualizar Status - Pendente para Cancelado com Sucesso (estorno de saldo)")
        void atualizarStatusPendenteParaCancelado() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 4.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setStatus("pendente");

            AgendamentoStatusRequest statusRequest = new AgendamentoStatusRequest();
            statusRequest.setStatus("cancelado");
            statusRequest.setObservacao("Cancelado por motivo de ir ver mãe solteira(ou não)");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
            when(saldoRepository.save(any(Saldo.class))).thenReturn(saldo);
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

            AgendamentoResponse response = agendamentoService.atualizarStatusAgendamentoPorId(1L, statusRequest);

            assertNotNull(response);
            verify(saldoRepository).save(any(Saldo.class));
        }

        @Test
        @DisplayName("Atualizar Status Falho - Status vazio")
        void atualizarStatusVazio() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);

            AgendamentoStatusRequest statusRequest = new AgendamentoStatusRequest();
            statusRequest.setStatus("   ");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.atualizarStatusAgendamentoPorId(1L, statusRequest)
            );
            assertEquals(400, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Status Falho - Transição inválida (pendente → finalizado)")
        void atualizarStatusTransicaoInvalida() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setStatus("pendente");

            AgendamentoStatusRequest statusRequest = new AgendamentoStatusRequest();
            statusRequest.setStatus("finalizado");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.atualizarStatusAgendamentoPorId(1L, statusRequest)
            );
            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Status Falho - Agendamento já finalizado não pode ser alterado")
        void atualizarStatusAgendamentoJaFinalizado() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setStatus("finalizado");

            AgendamentoStatusRequest statusRequest = new AgendamentoStatusRequest();
            statusRequest.setStatus("cancelado");
            statusRequest.setObservacao("Tentando cancelar após finalizar");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.atualizarStatusAgendamentoPorId(1L, statusRequest)
            );
            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Status Falho - Cancelar sem observação")
        void atualizarStatusCancelarSemObservacao() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setStatus("pendente");

            AgendamentoStatusRequest statusRequest = new AgendamentoStatusRequest();
            statusRequest.setStatus("cancelado");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.atualizarStatusAgendamentoPorId(1L, statusRequest)
            );
            assertEquals(400, exception.getStatusCode().value());
        }
    }

    @Nested
    class ConfirmarAgendamentosAutomaticamente {

        @Test
        @DisplayName("Confirmar Agendamento Automático - Agendamento dentro do limite de 24h")
        void confirmarAgendamentoQueDeveSer() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();

            // Data no passado → limite de confirmação já passou → confirma
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setData(LocalDate.now().minusDays(1));
            agendamento.setHoraInicio(LocalTime.of(10, 0));
            agendamento.setStatus("pendente");

            when(agendamentoRepository.findByStatusIn(List.of("pendente"))).thenReturn(List.of(agendamento));
            when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

            agendamentoService.confirmarAgendamentosAutomaticamente();

            verify(agendamentoRepository).save(agendamento);
            assertEquals("confirmado", agendamento.getStatus());
        }

        @Test
        @DisplayName("Confirmar Agendamento Automático - Agendamento fora do limite (não confirma)")
        void naoConfirmarAgendamentoFuturoDistante() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();

            // Data muito no futuro → limite ainda não chegou → não confirma
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setData(LocalDate.now().plusDays(5));
            agendamento.setHoraInicio(LocalTime.of(10, 0));
            agendamento.setStatus("pendente");

            when(agendamentoRepository.findByStatusIn(List.of("pendente"))).thenReturn(List.of(agendamento));

            agendamentoService.confirmarAgendamentosAutomaticamente();

            verify(agendamentoRepository, never()).save(agendamento);
            assertEquals("pendente", agendamento.getStatus());
        }
    }

    @Nested
    class BuscarPorStatus {

        @Test
        @DisplayName("Buscar Agendamentos por Status com Sucesso")
        void buscarAgendamentosPorStatus() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);

            when(agendamentoRepository.findAllByStatus("pendente")).thenReturn(List.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));

            List<AgendamentoResponse> response = agendamentoService.buscarAgendamentoPorStatus("pendente");

            assertNotNull(response);
            assertEquals(1, response.size());
            assertEquals("pendente", response.get(0).getStatus());
        }
    }

    @Nested
    class DeletarAgendamento {

        @Test
        @DisplayName("Deletar Agendamento com Sucesso")
        void deletarAgendamento() {
            Usuario aluno = criarAluno();
            Usuario professor = criarProfessor();
            Servico servico = criarServico();
            Condominio condominio = criarCondominio();
            Saldo saldo = criarSaldo(aluno, servico, 5.0);
            Agendamento agendamento = criarAgendamentoSalvo(aluno, professor, servico, condominio);
            agendamento.setStatus("pendente");

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
            when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L)).thenReturn(Optional.of(saldo));
            when(saldoRepository.save(any(Saldo.class))).thenReturn(saldo);

            agendamentoService.deletarAgendamentoPorId(1L);

            verify(agendamentoRepository).deleteById(1L);
            verify(saldoRepository).save(any(Saldo.class));
        }

        @Test
        @DisplayName("Deletar Agendamento Falho - Não encontrado")
        void deletarAgendamentoNaoEncontrado() {
            when(agendamentoRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> agendamentoService.deletarAgendamentoPorId(99L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }
}
