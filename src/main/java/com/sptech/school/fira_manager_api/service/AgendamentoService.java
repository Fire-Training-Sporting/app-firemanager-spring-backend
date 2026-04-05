package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.AgendamentoAtualizarDTO;
import com.sptech.school.fira_manager_api.dto.AgendamentoDTO;
import com.sptech.school.fira_manager_api.dto.AgendamentoStatusDTO;
import com.sptech.school.fira_manager_api.dto.responses.*;
import com.sptech.school.fira_manager_api.model.*;
import com.sptech.school.fira_manager_api.observer.AgendamentoSubject;
import com.sptech.school.fira_manager_api.observer.AlunoObserver;
import com.sptech.school.fira_manager_api.observer.ProfessorObserver;
import com.sptech.school.fira_manager_api.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;


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

    private void notificar(Agendamento agendamento) {
        AgendamentoSubject subject = new AgendamentoSubject();

        subject.addObserver(new AlunoObserver(agendamento.getAluno().getId(), emailService));

        subject.addObserver(new ProfessorObserver(agendamento.getProfessor().getId(), emailService));

        subject.notifyObservers(agendamento);
    }

    public AgendamentoResponse criarAgendamento(AgendamentoDTO dto) {

        Agendamento agendamentoNovo = new Agendamento();

        Saldo saldo = saldoRepository.findByAlunoIdAndServicoId(dto.getAluno(), dto.getServico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo não encontrado"));

        if (saldo.getQuantidade() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }

        Usuario usuarioAluno = buscarUsuario(dto.getAluno(), "Aluno");
        agendamentoNovo.setAluno(usuarioAluno);

        Usuario usuarioProfessor = buscarUsuario(dto.getProfessor(), "Professor");
        agendamentoNovo.setProfessor(usuarioProfessor);

        Usuario usuarioAuxiliar = dto.getAuxiliar() != null ? buscarUsuario(dto.getAuxiliar(), "Auxiliar") : null;
        agendamentoNovo.setAuxiliar(usuarioAuxiliar);

        Servico servico = buscarServico(dto.getServico());
        agendamentoNovo.setServico(servico);

        Condominio condominio = buscarCondominio(dto.getCondominio());
        agendamentoNovo.setCondominio(condominio);

        agendamentoNovo.setData(dto.getData());
        agendamentoNovo.setHoraInicio(dto.getHoraInicio());
        agendamentoNovo.setObservacao(dto.getObservacao());

        saldo.setQuantidade(saldo.getQuantidade() - 1);

        saldoRepository.save(saldo);
        agendamentoNovo = agendamentoRepository.save(agendamentoNovo);

        notificar(agendamentoNovo);

        return toAgendamentoResponse(agendamentoNovo, saldo);
    }

    public List<AgendamentoResponse> listarAgendamento() {
        return agendamentoRepository.findAll()
                .stream()
                .map(this::toAgendamentoResponse)
                .toList();
    }

    public AgendamentoResponse listarAgendamentoPorId(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        return toAgendamentoResponse(agendamento);
    }


    public AgendamentoResponse atualizarAgendamentoPorId(AgendamentoAtualizarDTO dto, Long id) {

        Agendamento agendamentoNovo = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Agendamento não encontrado"
                ));

        if (!"pendente".equals(agendamentoNovo.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O agendamento não pode ser atualizado");
        }

        Servico servicoAntigo = agendamentoNovo.getServico();

        Servico servicoNovo = buscarServico(dto.getServico());

        Saldo saldo = null;

        if (!servicoAntigo.getId().equals(servicoNovo.getId())) {

            Saldo saldoAntigo = buscarSaldo(dto.getAluno(), servicoAntigo.getId());
            Saldo saldoNovo = buscarSaldo(dto.getAluno(), servicoNovo.getId());

            if (saldoNovo.getQuantidade() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para o novo serviço");
            }

            saldoAntigo.setQuantidade(saldoAntigo.getQuantidade() + 1);
            saldoNovo.setQuantidade(saldoNovo.getQuantidade() - 1);

            saldoRepository.save(saldoAntigo);
            saldoRepository.save(saldoNovo);

            saldo = saldoNovo;
        }

        Usuario usuarioAluno = buscarUsuario(dto.getAluno(), "Aluno");
        agendamentoNovo.setAluno(usuarioAluno);

        Usuario usuarioProfessor = buscarUsuario(dto.getProfessor(), "Professor");
        agendamentoNovo.setProfessor(usuarioProfessor);

        Usuario usuarioAuxiliar = dto.getAuxiliar() != null ? buscarUsuario(dto.getAuxiliar(), "Auxiliar") : null;
        agendamentoNovo.setAuxiliar(usuarioAuxiliar);

        Condominio condominio = buscarCondominio(dto.getCondominio());
        agendamentoNovo.setCondominio(condominio);

        agendamentoNovo.setServico(servicoNovo);
        agendamentoNovo.setData(dto.getData());
        agendamentoNovo.setHoraInicio(dto.getHoraInicio());
        agendamentoNovo.setObservacao(dto.getObservacao());

        Agendamento agendamento = agendamentoRepository.save(agendamentoNovo);

        return toAgendamentoResponse(agendamento, saldo);
    }

    public AgendamentoResponse atualizarStatusAgendamentoPorId(Long id, AgendamentoStatusDTO dto) {

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));


        Saldo saldo = buscarSaldo(agendamento.getAluno().getId(), agendamento.getServico().getId());

        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status não pode ser vazio");
        }

        String statusAtual = agendamento.getStatus().toLowerCase();

        if (statusAtual.equals("confirmado") || statusAtual.equals("finalizado")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Status não pode ser atualizado");
        }

        if (statusAtual.equals("cancelado")) {
            saldo.setQuantidade(saldo.getQuantidade() + 1);
            saldoRepository.save(saldo);
        }

        agendamento.setStatus(dto.getStatus());
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

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "O agendamento não existe!"
                ));

        Saldo saldo = buscarSaldo(agendamento.getAluno().getId(), agendamento.getServico().getId());

        saldo.setQuantidade(saldo.getQuantidade() + 1);
        saldoRepository.save(saldo);

        agendamentoRepository.deleteById(id);
    }
}