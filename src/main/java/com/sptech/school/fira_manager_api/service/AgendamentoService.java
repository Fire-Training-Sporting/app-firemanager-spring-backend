package com.sptech.school.fira_manager_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoRequest;
import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoRecorrenteRequest;
import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoStatusRequest;
import com.sptech.school.fira_manager_api.dto.responses.condominio.CondominioResponse;
import com.sptech.school.fira_manager_api.dto.responses.saldo.SaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.dto.responses.agendamento.AgendamentoResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.ProfessorResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.model.Agendamento;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.observer.AgendamentoSubject;
import com.sptech.school.fira_manager_api.observer.AlunoObserver;
import com.sptech.school.fira_manager_api.observer.ProfessorObserver;
import com.sptech.school.fira_manager_api.repository.AgendamentoRepository;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import com.sptech.school.fira_manager_api.repository.SaldoRepository;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;


@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CondominioRepository condominioRepository;
    private final ServicoRepository servicoRepository;
    private final SaldoRepository saldoRepository;
    private final EmailService emailService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository, CondominioRepository condominioRepository, ServicoRepository servicoRepository, SaldoRepository saldoRepository, EmailService emailService) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.condominioRepository = condominioRepository;
        this.servicoRepository = servicoRepository;
        this.saldoRepository = saldoRepository;
        this.emailService = emailService;
    }

    private ProfessorResponse toProfessorResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new ProfessorResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }

    private ServicoResponse toServicoResponse(Servico servico) {
        if (servico == null) {
            return null;
        }

        return new ServicoResponse(
                servico.getId(),
                servico.getNome()
        );
    }

    private SaldoResponse toSaldoResponse(Saldo saldo) {
        if (saldo == null) {
            return null;
        }

        return new SaldoResponse(
                saldo.getQuantidade(),
                toServicoResponse(saldo.getServico())
        );
    }

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }

    private CondominioResponse toCondomioResponse(Condominio condominio) {
        if (condominio == null) return null;

        return new CondominioResponse(
                condominio.getNome(),
                condominio.getCidade(),
                condominio.getBairro(),
                condominio.getLogradouro(),
                condominio.getNumero()
        );
    }

    private AgendamentoResponse toAgendamentoResponse(Agendamento agendamento) {
        Saldo saldo = saldoRepository
                .findByAlunoIdAndServicoId(agendamento.getAluno().getId(), agendamento.getServico().getId())
                .orElse(null);

        return toAgendamentoResponse(agendamento, saldo);
    }

    private AgendamentoResponse toAgendamentoResponse(Agendamento agendamento, Saldo saldo) {
        ProfessorResponse professorResponse = toProfessorResponse(agendamento.getProfessor());
        ServicoResponse servicoResponse = toServicoResponse(agendamento.getServico());
        SaldoResponse saldoResponse = toSaldoResponse(saldo);
        UsuarioResponse usuarioResponse = toUsuarioResponse(agendamento.getAluno());
        CondominioResponse condominioResponse = toCondomioResponse(agendamento.getCondominio());

        if (agendamento.getAuxiliar() != null) {
            ProfessorResponse auxiliarResponse = toProfessorResponse(agendamento.getAuxiliar());

            return new AgendamentoResponse(
                    agendamento.getId(),
                    usuarioResponse,
                    saldoResponse,
                    professorResponse,
                    auxiliarResponse,
                    servicoResponse,
                    condominioResponse,
                    agendamento.getData(),
                    agendamento.getHoraInicio(),
                    agendamento.getObservacao(),
                    agendamento.getCriadoEm(),
                    agendamento.getAtualizadoEm(),
                    agendamento.getStatus()
            );
        }

        return new AgendamentoResponse(
                agendamento.getId(),
                usuarioResponse,
                saldoResponse,
                professorResponse,
                servicoResponse,
                condominioResponse,
                agendamento.getData(),
                agendamento.getHoraInicio(),
                agendamento.getObservacao(),
                agendamento.getCriadoEm(),
                agendamento.getAtualizadoEm(),
                agendamento.getStatus()
        );
    }


    private Usuario buscarUsuario(Long id, String tipo) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, tipo + " não encontrado"));
    }

    private Servico buscarServico(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    private Condominio buscarCondominio(Long id) {
        return condominioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condominio não encontrado"));
    }

    private Saldo buscarSaldo(Long alunoId, Long servicoId) {
        return saldoRepository.findByAlunoIdAndServicoId(alunoId, servicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo não encontrado"));
    }

    private Agendamento buscarAgendamento(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));
    }

    private Usuario buscarAuxiliar(Long auxiliarId) {
        if (auxiliarId == null) {
            return null;
        }

        return buscarUsuario(auxiliarId, "Auxiliar");
    }

    private void preencherAgendamento(
        Agendamento agendamento,
        Long alunoId,
        Long professorId,
        Long auxiliarId,
        Long servicoId,
        Long condominioId,
        AgendamentoRequest dto)   
    {
        agendamento.setAluno(buscarUsuario(alunoId, "Aluno"));
        agendamento.setProfessor(buscarUsuario(professorId, "Professor"));
        agendamento.setAuxiliar(buscarAuxiliar(auxiliarId));
        agendamento.setServico(buscarServico(servicoId));
        agendamento.setCondominio(buscarCondominio(condominioId));
        agendamento.setData(dto.getData());
        agendamento.setHoraInicio(dto.getHoraInicio());
        agendamento.setObservacao(dto.getObservacao());
    }

    private void notificar(Agendamento agendamento) {
        AgendamentoSubject subject = new AgendamentoSubject();

        subject.addObserver(new AlunoObserver(agendamento.getAluno().getId(), emailService));
        subject.addObserver(new ProfessorObserver(agendamento.getProfessor().getId(), emailService));
        subject.notifyObservers(agendamento);
    }

    private Double calcularCustoSaldo(LocalTime horaInicio, LocalTime horaFim) {
        long minutos = java.time.Duration.between(horaInicio, horaFim).toMinutes();

        if (minutos <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hora fim deve ser depois da hora início");
        }

        if (minutos % 30 != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O intervalo deve ser em 30 minutos");
        }

        return minutos / 60.0;
    }

    private void validarConflitoProfessor(Long professorId, Long condominioId, LocalDate data, LocalTime horaInicio, LocalTime horaFim, Long agendamentoIdExcluir) {

        List<Agendamento> agendamentosDoDia = agendamentoRepository
                .findByProfessorIdAndDataAndStatusNot(professorId, data, "cancelado");

        List<Agendamento> sobreposicao = agendamentosDoDia.stream()
                .filter(existente -> agendamentoIdExcluir == null || !existente.getId().equals(agendamentoIdExcluir))
                .filter(existente -> existente.getCondominio().getId().equals(condominioId))
                .filter(existente -> horaInicio.isBefore(existente.getHoraFim()))
                .filter(existente -> horaFim.isAfter(existente.getHoraInicio()))
                .toList();

        if (!sobreposicao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Professor já possui um agendamento nesse horário");
        }

        List<Agendamento> conflitoIntervalo = agendamentosDoDia.stream()
                .filter(existente -> agendamentoIdExcluir == null || !existente.getId().equals(agendamentoIdExcluir))
                .filter(existente -> !existente.getCondominio().getId().equals(condominioId))
                .filter(existente -> horaInicio.isBefore(existente.getHoraFim().plusHours(1)))
                .filter(existente -> horaFim.isAfter(existente.getHoraInicio().minusHours(1)))
                .toList();

        if (!conflitoIntervalo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Professor precisa de no mínimo 1h de intervalo entre condominios diferentes");
        }
    }

    @Transactional
    public AgendamentoResponse criarAgendamento(AgendamentoRequest dto) {
        Agendamento agendamento = new Agendamento();

        Saldo saldo = buscarSaldo(dto.getAluno(), dto.getServico());
        Double custo = calcularCustoSaldo(dto.getHoraInicio(), dto.getHoraFim());

        if (saldo.getQuantidade() < custo) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }

        validarConflitoProfessor(
                dto.getProfessor(),
                dto.getCondominio(),
                dto.getData(),
                dto.getHoraInicio(),
                dto.getHoraFim(),
                null
        );

        preencherAgendamento(
                agendamento,
                dto.getAluno(),
                dto.getProfessor(),
                dto.getAuxiliar(),
                dto.getServico(),
                dto.getCondominio(),
                dto
        );

        agendamento.setHoraFim(dto.getHoraFim());

        saldo.setQuantidade(saldo.getQuantidade() - custo);

        saldoRepository.save(saldo);
        agendamento = agendamentoRepository.save(agendamento);

        notificar(agendamento);

        return toAgendamentoResponse(agendamento, saldo);
    }

    @Transactional
    public List<AgendamentoResponse> criarAgendamentoRecorrente(AgendamentoRecorrenteRequest dto) {
        Saldo saldo = buscarSaldo(dto.getAluno(), dto.getServico());
        Double custo = calcularCustoSaldo(dto.getHoraInicio(), dto.getHoraFim());
        Double custoTotal = custo * dto.getQuantidadeRecorrencias();

        if (saldo.getQuantidade() < custo) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }

        if (saldo.getQuantidade() < custoTotal) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de recorrências excede o saldo disponível"
            );
        }

        int quantidadeRecorrencias = dto.getQuantidadeRecorrencias();
        List<AgendamentoResponse> agendamentosCriados = new ArrayList<>();

        for (int i = 0; i < quantidadeRecorrencias; i++) {
            LocalDate dataRecorrente = dto.getData().plusDays(i * 7L);

            validarConflitoProfessor(
                    dto.getProfessor(),
                    dto.getCondominio(),
                    dataRecorrente,
                    dto.getHoraInicio(),
                    dto.getHoraFim(),
                    null
            );

            Agendamento agendamento = new Agendamento();

            preencherAgendamento(
                    agendamento,
                    dto.getAluno(),
                    dto.getProfessor(),
                    dto.getAuxiliar(),
                    dto.getServico(),
                    dto.getCondominio(),
                    dto
            );

            agendamento.setData(dataRecorrente);
            agendamento.setHoraFim(dto.getHoraFim());

            saldo.setQuantidade(saldo.getQuantidade() - custo);

            agendamento = agendamentoRepository.save(agendamento);

            notificar(agendamento);

            agendamentosCriados.add(toAgendamentoResponse(agendamento, saldo));
        }

        saldoRepository.save(saldo);

        return agendamentosCriados;
    }

    public List<AgendamentoResponse> listarAgendamento() {
        return agendamentoRepository.findAll()
                .stream()
                .map(this::toAgendamentoResponse)
                .toList();
    }

    public AgendamentoResponse listarAgendamentoPorId(Long id) {
        return toAgendamentoResponse(buscarAgendamento(id));
    }


    public AgendamentoResponse atualizarAgendamentoPorId(AgendamentoRequest dto, Long id) {
        Agendamento agendamento = buscarAgendamento(id);

        if (!"pendente".equals(agendamento.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O agendamento não pode ser atualizado");
        }

        Servico servicoAntigo = agendamento.getServico();
        Servico servicoNovo = buscarServico(dto.getServico());

        Saldo saldo = buscarSaldo(dto.getAluno(), servicoAntigo.getId());

        if (!servicoAntigo.getId().equals(servicoNovo.getId())) {
            Saldo saldoNovo = buscarSaldo(dto.getAluno(), servicoNovo.getId());
            Double custo = calcularCustoSaldo(agendamento.getHoraInicio(), agendamento.getHoraFim());

            if (saldoNovo.getQuantidade() < custo) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para o novo serviço");
            }

            saldo.setQuantidade(saldo.getQuantidade() + custo);
            saldoNovo.setQuantidade(saldoNovo.getQuantidade() - custo);

            saldoRepository.save(saldo);
            saldoRepository.save(saldoNovo);

            saldo = saldoNovo;
        }

        agendamento.setAluno(buscarUsuario(dto.getAluno(), "Aluno"));
        agendamento.setProfessor(buscarUsuario(dto.getProfessor(), "Professor"));
        agendamento.setAuxiliar(buscarAuxiliar(dto.getAuxiliar()));
        agendamento.setCondominio(buscarCondominio(dto.getCondominio()));
        agendamento.setServico(servicoNovo);
        agendamento.setData(dto.getData());
        agendamento.setHoraInicio(dto.getHoraInicio());
        agendamento.setObservacao(dto.getObservacao());

        agendamento = agendamentoRepository.save(agendamento);

        return toAgendamentoResponse(agendamento, saldo);
    }
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void confirmarAgendamentosAutomaticamente() {
        List<Agendamento> agendamentos = agendamentoRepository.findByStatusIn(List.of("pendente"));

        LocalDateTime agora = LocalDateTime.now();

        for (Agendamento agendamento : agendamentos) {
            LocalDateTime dataHoraAgendamento = LocalDateTime.of(
                    agendamento.getData(),
                    agendamento.getHoraInicio()
            );

            LocalDateTime limiteConfirmacao = dataHoraAgendamento.minusHours(24);

            if (!agora.isBefore(limiteConfirmacao)) {
                agendamento.setStatus("confirmado");
                agendamento.setAtualizadoEm(agora);

                agendamentoRepository.save(agendamento);

                notificar(agendamento);
            }
        }
    }

    public AgendamentoResponse atualizarStatusAgendamentoPorId(Long id, AgendamentoStatusRequest dto) {
        Agendamento agendamento = buscarAgendamento(id);

        Saldo saldo = buscarSaldo(
                agendamento.getAluno().getId(),
                agendamento.getServico().getId()
        );

        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status não pode ser vazio");
        }

        String statusAtual = agendamento.getStatus().trim().toLowerCase();
        String statusNovo = dto.getStatus().trim().toLowerCase();

        if (statusAtual.equals("finalizado") || statusAtual.equals("cancelado")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Agendamentos finalizados ou cancelados não podem ter o status alterado");
        }

        boolean transicaoValida = (statusAtual.equals("pendente") &&
                (statusNovo.equals("confirmado") || statusNovo.equals("cancelado")))
                ||
                (statusAtual.equals("confirmado") &&
                        (statusNovo.equals("finalizado") || statusNovo.equals("cancelado")));

        if (!transicaoValida) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Transição de status não permitida");
        }

        if (statusNovo.equals("cancelado")) {
            if (dto.getObservacao() == null || dto.getObservacao().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A justificativa do cancelamento é obrigatória");
            }

            Double custoEstorno = calcularCustoSaldo(agendamento.getHoraInicio(), agendamento.getHoraFim());
            saldo.setQuantidade(saldo.getQuantidade() + custoEstorno);
            saldoRepository.save(saldo);

            agendamento.setObservacao(dto.getObservacao().trim());
        }

        agendamento.setStatus(statusNovo);
        agendamento.setAtualizadoEm(LocalDateTime.now());

        agendamento = agendamentoRepository.save(agendamento);

        notificar(agendamento);

        return toAgendamentoResponse(agendamento);
    }

    public List<AgendamentoResponse> buscarAgendamentoPorStatus(String status) {
        return agendamentoRepository.findAllByStatus(status)
                .stream()
                .map(this::toAgendamentoResponse)
                .toList();
    }

    public void deletarAgendamentoPorId(Long id) {
        Agendamento agendamento = buscarAgendamento(id);

        Saldo saldo = buscarSaldo(
                agendamento.getAluno().getId(),
                agendamento.getServico().getId()
        );

        Double custoEstorno = calcularCustoSaldo(agendamento.getHoraInicio(), agendamento.getHoraFim());
        saldo.setQuantidade(saldo.getQuantidade() + custoEstorno);
        saldoRepository.save(saldo);

        agendamentoRepository.deleteById(id);
    }
}