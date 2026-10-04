package com.sptech.school.fira_manager_api.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.JoinType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import com.sptech.school.fira_manager_api.dto.responses.agendamento.HistoricoAulasResponse;
import com.sptech.school.fira_manager_api.dto.responses.PaginaResponse;
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
                condominio.getRua(),
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

        AgendamentoResponse response;
        if (agendamento.getAuxiliar() != null) {
            ProfessorResponse auxiliarResponse = toProfessorResponse(agendamento.getAuxiliar());

            response = new AgendamentoResponse(
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
        } else {
            response = new AgendamentoResponse(
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

        response.setRebatedor(toProfessorResponse(agendamento.getRebatedor()));
        return response;
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

    private Usuario buscarRebatedor(Long rebatedorId) {
        if (rebatedorId == null) {
            return null;
        }

        return buscarUsuario(rebatedorId, "Rebatedor");
    }

    private void preencherAgendamento(
        Agendamento agendamento,
        Long alunoId,
        Long professorId,
        Long auxiliarId,
        Long rebatedorId,
        Long servicoId,
        Long condominioId,
        AgendamentoRequest dto)   
    {
        agendamento.setAluno(buscarUsuario(alunoId, "Aluno"));
        agendamento.setProfessor(buscarUsuario(professorId, "Professor"));
        agendamento.setAuxiliar(buscarAuxiliar(auxiliarId));
        agendamento.setRebatedor(buscarRebatedor(rebatedorId));
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

    @Transactional
    public AgendamentoResponse criarAgendamento(AgendamentoRequest dto) {
        Agendamento agendamento = new Agendamento();

        Saldo saldo = buscarSaldo(dto.getAluno(), dto.getServico());
        Double custo = calcularCustoSaldo(dto.getHoraInicio(), dto.getHoraFim());

        if (saldo.getQuantidade() < custo) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }

        preencherAgendamento(
                agendamento,
                dto.getAluno(),
                dto.getProfessor(),
                dto.getAuxiliar(),
                dto.getRebatedor(),
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
            Agendamento agendamento = new Agendamento();

            preencherAgendamento(
                    agendamento,
                    dto.getAluno(),
                    dto.getProfessor(),
                    dto.getAuxiliar(),
                    null,
                    dto.getServico(),
                    dto.getCondominio(),
                    dto
            );

            agendamento.setData(dto.getData().plusDays(i * 7L));
            agendamento.setHoraFim(dto.getHoraFim());

            saldo.setQuantidade(saldo.getQuantidade() - custo);

            agendamento = agendamentoRepository.save(agendamento);

            notificar(agendamento);

            agendamentosCriados.add(toAgendamentoResponse(agendamento, saldo));
        }

        saldoRepository.save(saldo);

        return agendamentosCriados;
    }

    public PaginaResponse<AgendamentoResponse> listarAgendamento(Pageable pageable) {
        Page<AgendamentoResponse> pagina = agendamentoRepository.findAll(comOrdenacaoEstavel(pageable))
                .map(this::toAgendamentoResponse);
        return PaginaResponse.from(pagina);
    }

    public PaginaResponse<AgendamentoResponse> buscarAgendamentosPaginados(
            Pageable pageable, String status, String campo, String busca) {
        String campoBusca = campo == null || campo.isBlank() ? "id" : campo;
        String termo = busca == null || busca.isBlank() ? null : busca.trim().toLowerCase(Locale.ROOT);
        List<String> camposPermitidos = List.of("aluno", "id", "data", "condominio", "professor", "status");

        if (termo != null && !camposPermitidos.contains(campoBusca)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de busca inválido");
        }

        Specification<Agendamento> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null && !status.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("status")), status.trim().toLowerCase(Locale.ROOT)));
            }

            if (termo != null) {
                String padrao = "%" + termo + "%";
                switch (campoBusca) {
                    case "id" -> predicates.add(criteriaBuilder.like(root.get("id").as(String.class), padrao));
                    case "data" -> predicates.add(criteriaBuilder.like(root.get("data").as(String.class), padrao));
                    case "status" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("status")), padrao));
                    case "aluno" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.join("aluno").get("nome")), padrao));
                    case "condominio" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.join("condominio").get("nome")), padrao));
                    case "professor" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.join("professor").get("nome")), padrao));
                    default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de busca inválido");
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<AgendamentoResponse> pagina = agendamentoRepository
                .findAll(specification, comOrdenacaoEstavel(pageable))
                .map(this::toAgendamentoResponse);
        return PaginaResponse.from(pagina);
    }

    public HistoricoAulasResponse buscarHistoricoAulasPaginado(
            Long participanteId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        Specification<Agendamento> filtroDatas = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dataInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("data"), dataInicio));
            }
            if (dataFim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("data"), dataFim));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Specification<Agendamento> filtroParticipacao = (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.equal(root.join("professor", JoinType.LEFT).get("id"), participanteId),
                criteriaBuilder.equal(root.join("rebatedor", JoinType.LEFT).get("id"), participanteId),
                criteriaBuilder.equal(root.join("auxiliar", JoinType.LEFT).get("id"), participanteId));

        Specification<Agendamento> filtroHistorico = filtroDatas.and(filtroParticipacao);
        Page<Agendamento> paginaAgendamentos = agendamentoRepository
                .findAll(filtroHistorico, comOrdenacaoEstavel(pageable));

        long aulasComoProfessor = agendamentoRepository.count(
                filtroDatas.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.join("professor", JoinType.LEFT).get("id"), participanteId)));
        long aulasComoRebatedor = agendamentoRepository.count(
                filtroDatas.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.join("rebatedor", JoinType.LEFT).get("id"), participanteId)));
        long aulasComoAuxiliar = agendamentoRepository.count(
                filtroDatas.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.join("auxiliar", JoinType.LEFT).get("id"), participanteId)));

        PaginaResponse<AgendamentoResponse> paginaResponse = PaginaResponse.from(
                paginaAgendamentos.map(this::toAgendamentoResponse));
        return new HistoricoAulasResponse(
                paginaResponse,
                aulasComoProfessor,
                aulasComoRebatedor,
                aulasComoAuxiliar);
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
        agendamento.setRebatedor(buscarRebatedor(dto.getRebatedor()));
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

    public PaginaResponse<AgendamentoResponse> buscarAgendamentoPorStatus(String status, Pageable pageable) {
        Page<AgendamentoResponse> pagina = agendamentoRepository
                .findAllByStatus(status, comOrdenacaoEstavel(pageable))
                .map(this::toAgendamentoResponse);
        return PaginaResponse.from(pagina);
    }

    private Pageable comOrdenacaoEstavel(Pageable pageable) {
        List<Sort.Order> outrasOrdenacoes = pageable.getSort().stream()
                .filter(order -> !order.getProperty().equals("id"))
                .toList();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        if (!outrasOrdenacoes.isEmpty()) {
            sort = sort.and(Sort.by(outrasOrdenacoes));
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
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