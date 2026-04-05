package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.AgendamentoAtualizarDTO;
import com.sptech.school.fira_manager_api.dto.AgendamentoDTO;
import com.sptech.school.fira_manager_api.dto.AgendamentoStatusDTO;
import com.sptech.school.fira_manager_api.dto.SaldoDTO;
import com.sptech.school.fira_manager_api.dto.responses.AgendamentoResponse;
import com.sptech.school.fira_manager_api.dto.responses.ProfessorResponse;
import com.sptech.school.fira_manager_api.dto.responses.SaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.ServicoResponse;
import com.sptech.school.fira_manager_api.model.*;
import com.sptech.school.fira_manager_api.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CondominioRepository condominioRepository;
    private final ServicoRepository servicoRepository;
    private final SaldoRepository saldoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              UsuarioRepository usuarioRepository,
                              CondominioRepository condominioRepository,
                              ServicoRepository servicoRepository, SaldoRepository saldoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.condominioRepository = condominioRepository;
        this.servicoRepository = servicoRepository;
        this.saldoRepository = saldoRepository;
    }

    private ProfessorResponse toProfessorResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new ProfessorResponse(
                usuario.getId(),
                usuario.getTipoUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCriadoEm()
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

    private SaldoResponse toSaldoResponse(Saldo saldo){
        if (saldo == null){
            return null;
        }

        return new SaldoResponse(
                saldo.getId(),
                saldo.getAluno(),
                saldo.getQuantidade(),
                saldo.getServico()
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

        if (agendamento.getAuxiliar() != null) {
            ProfessorResponse auxiliarResponse = toProfessorResponse(agendamento.getAuxiliar());

            return new AgendamentoResponse(
                    agendamento.getId(),
                    agendamento.getAluno(),
                    saldoResponse,
                    professorResponse,
                    auxiliarResponse,
                    servicoResponse,
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
                agendamento.getAluno(),
                saldoResponse,
                professorResponse,
                servicoResponse,
                agendamento.getData(),
                agendamento.getHoraInicio(),
                agendamento.getObservacao(),
                agendamento.getCriadoEm(),
                agendamento.getAtualizadoEm(),
                agendamento.getStatus()
        );
    }

    public AgendamentoResponse criarAgendamento(AgendamentoDTO dto) {

        Agendamento agendamentoNovo = new Agendamento();

        Saldo saldo = saldoRepository
                .findByAlunoIdAndServicoId(dto.getAluno(), dto.getServico())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Saldo não encontrado"
                ));

        // 🔹 valida saldo
        if (saldo.getQuantidade() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Saldo insuficiente"
            );
        }

        Usuario usuarioAluno = usuarioRepository.findById(dto.getAluno())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        agendamentoNovo.setAluno(usuarioAluno);

        Usuario usuarioProfessor = usuarioRepository.findById(dto.getProfessor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
        agendamentoNovo.setProfessor(usuarioProfessor);

        if (dto.getAuxiliar() != null) {
            Usuario usuarioAuxiliar = usuarioRepository.findById(dto.getAuxiliar())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auxiliar não encontrado"));
            agendamentoNovo.setAuxiliar(usuarioAuxiliar);
        } else {
            agendamentoNovo.setAuxiliar(null);
        }

        Servico servico = servicoRepository.findById(dto.getServico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
        agendamentoNovo.setServico(servico);

        Condominio condominio = condominioRepository.findById(dto.getCondominio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condominio não encontrado"));
        agendamentoNovo.setCondominio(condominio);

        agendamentoNovo.setData(dto.getData());
        agendamentoNovo.setHoraInicio(dto.getHoraInicio());
        agendamentoNovo.setObservacao(dto.getObservacao());

        saldo.setQuantidade(saldo.getQuantidade() - 1);

        saldoRepository.save(saldo);
        agendamentoNovo = agendamentoRepository.save(agendamentoNovo);

        return toAgendamentoResponse(agendamentoNovo, saldo);
    }

    public List<AgendamentoResponse> listarAgendamento() {
        return agendamentoRepository.findAll()
                .stream()
                .map(this::toAgendamentoResponse)
                .toList();
    }

    public AgendamentoResponse listarAgendamentoPorId(Long id){
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

        Servico servicoNovo = servicoRepository.findById(dto.getServico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        Saldo saldo = null;

        if (!servicoAntigo.getId().equals(servicoNovo.getId())) {

            Saldo saldoAntigo = saldoRepository
                    .findByAlunoIdAndServicoId(dto.getAluno(), servicoAntigo.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo antigo não encontrado"));

            Saldo saldoNovo = saldoRepository
                    .findByAlunoIdAndServicoId(dto.getAluno(), servicoNovo.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo novo não encontrado"));

            if (saldoNovo.getQuantidade() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Saldo insuficiente para o novo serviço"
                );
            }

            saldoAntigo.setQuantidade(saldoAntigo.getQuantidade() + 1);
            saldoNovo.setQuantidade(saldoNovo.getQuantidade() - 1);

            saldoRepository.save(saldoAntigo);
            saldoRepository.save(saldoNovo);

            saldo = saldoNovo;
        }

        Usuario usuarioAluno = usuarioRepository.findById(dto.getAluno())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        agendamentoNovo.setAluno(usuarioAluno);

        Usuario usuarioProfessor = usuarioRepository.findById(dto.getProfessor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
        agendamentoNovo.setProfessor(usuarioProfessor);

        if (dto.getAuxiliar() == null) {
            agendamentoNovo.setAuxiliar(null);
        } else {
            Usuario usuarioAuxiliar = usuarioRepository.findById(dto.getAuxiliar())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auxiliar não encontrado"));
            agendamentoNovo.setAuxiliar(usuarioAuxiliar);
        }

        Condominio condominio = condominioRepository.findById(dto.getCondominio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condominio não encontrado"));
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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Agendamento não encontrado"
                ));

        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status não pode ser vazio");
        }

        agendamento.setStatus(dto.getStatus().trim().toLowerCase());

        agendamento = agendamentoRepository.save(agendamento);

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

        Saldo saldo = saldoRepository.findByAlunoIdAndServicoId(agendamento.getAluno().getId(),
                        agendamento.getServico().getId()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Saldo não encontrado"
                ));

        saldo.setQuantidade(saldo.getQuantidade() + 1);
        saldoRepository.save(saldo);

        agendamentoRepository.deleteById(id);
    }


}