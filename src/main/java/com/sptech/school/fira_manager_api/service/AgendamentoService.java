package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.AgendamentoDTO;
import com.sptech.school.fira_manager_api.model.*;
import com.sptech.school.fira_manager_api.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CondominioRepository condominioRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              UsuarioRepository usuarioRepository,
                              CondominioRepository condominioRepository,
                              ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.condominioRepository = condominioRepository;
        this.servicoRepository = servicoRepository;
    }

    public AgendamentoDTO criar(AgendamentoDTO dto) {
        validarData(dto.getData());

        Usuario aluno = buscarUsuario(dto.getAlunoId());
        validarAluno(aluno);

        Usuario professor = buscarUsuario(dto.getProfessorId());
        validarProfessor(professor);

        Usuario auxiliar = null;
        if (dto.getAuxiliarId() != null) {
            auxiliar = buscarUsuario(dto.getAuxiliarId());
            validarProfessor(auxiliar);
        }

        Condominio condominio = buscarCondominio(dto.getCondominioId());
        Servico servico = buscarServico(dto.getServicoId());

        validarConflitoProfessor(professor, dto.getData());

        Agendamento agendamento = new Agendamento();
        agendamento.setAluno(aluno);
        agendamento.setProfessor(professor);
        agendamento.setAuxiliar(auxiliar);
        agendamento.setCondominio(condominio);
        agendamento.setServico(servico);
        agendamento.setData(dto.getData());
        agendamento.setObservacao(dto.getObservacao());

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return converterParaDTO(salvo);
    }

    public List<AgendamentoDTO> listar() {
        return agendamentoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public AgendamentoDTO atualizar(Long id, AgendamentoDTO dto) {
        Agendamento agendamento = buscarAgendamento(id);

        validarData(dto.getData());

        Usuario aluno = buscarUsuario(dto.getAlunoId());
        validarAluno(aluno);

        Usuario professor = buscarUsuario(dto.getProfessorId());
        validarProfessor(professor);

        Usuario auxiliar = null;
        if (dto.getAuxiliarId() != null) {
            auxiliar = buscarUsuario(dto.getAuxiliarId());
            validarProfessor(auxiliar);
        }

        Condominio condominio = buscarCondominio(dto.getCondominioId());
        Servico servico = buscarServico(dto.getServicoId());

        agendamento.setAluno(aluno);
        agendamento.setProfessor(professor);
        agendamento.setAuxiliar(auxiliar);
        agendamento.setCondominio(condominio);
        agendamento.setServico(servico);
        agendamento.setData(dto.getData());
        agendamento.setObservacao(dto.getObservacao());

        Agendamento atualizado = agendamentoRepository.save(agendamento);
        return converterParaDTO(atualizado);
    }

    public void deletar(Long id) {
        Agendamento agendamento = buscarAgendamento(id);
        agendamentoRepository.delete(agendamento);
    }

    // === MÉTODOS PRIVADOS ===

    private Agendamento buscarAgendamento(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private Condominio buscarCondominio(Long id) {
        return condominioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
    }

    private Servico buscarServico(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }

    private void validarAluno(Usuario usuario) {
        if (!usuario.getTipoUsuario().getId().equals(4L)) {
            throw new RuntimeException("Usuário informado não é um aluno");
        }
    }

    private void validarProfessor(Usuario usuario) {
        if (!usuario.getTipoUsuario().getId().equals(2L)) {
            throw new RuntimeException("Usuário informado não é um professor");
        }
    }

    private void validarData(LocalDateTime data) {
        if (data.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A data do agendamento não pode ser no passado");
        }
    }

    private void validarConflitoProfessor(Usuario professor, LocalDateTime data) {
        boolean temConflito = agendamentoRepository.findAll()
                .stream()
                .anyMatch(a -> a.getProfessor().getId().equals(professor.getId())
                        && a.getData().equals(data));

        if (temConflito) {
            throw new RuntimeException("Professor já possui agendamento neste horário");
        }
    }

    private AgendamentoDTO converterParaDTO(Agendamento agendamento) {
        return new AgendamentoDTO(
                agendamento.getAluno().getId(),
                agendamento.getProfessor().getId(),
                agendamento.getAuxiliar() != null ? agendamento.getAuxiliar().getId() : null,
                agendamento.getCondominio().getId(),
                agendamento.getServico().getId(),
                agendamento.getData(),
                agendamento.getObservacao()
        );
    }
}