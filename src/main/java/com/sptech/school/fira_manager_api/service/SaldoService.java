package com.sptech.school.fira_manager_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sptech.school.fira_manager_api.dto.requests.saldo.SaldoRequest;
import com.sptech.school.fira_manager_api.dto.responses.saldo.ProfessorSaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.saldo.SaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.saldo.ServicoProfessorResponse;
import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.ProfessorResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.AgendamentoRepository;
import com.sptech.school.fira_manager_api.repository.SaldoRepository;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;

@Service
public class SaldoService {

    private final SaldoRepository saldoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicoRepository servicoRepository;
    private final AgendamentoRepository agendamentoRepository;

    public SaldoService(SaldoRepository saldoRepository, UsuarioRepository usuarioRepository, ServicoRepository servicoRepository, AgendamentoRepository agendamentoRepository) {
        this.saldoRepository = saldoRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicoRepository = servicoRepository;
        this.agendamentoRepository = agendamentoRepository;
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

    private ServicoResponse toServicoResponse(Servico servico) {
        if (servico == null) return null;

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
                saldo.getId(),
                toUsuarioResponse(saldo.getAluno()),
                saldo.getQuantidade(),
                toServicoResponse(saldo.getServico())
        );
    }


    public SaldoResponse criarSaldo(SaldoRequest dto) {
        Saldo saldoNovo = new Saldo();

        Usuario saldoUsuario = usuarioRepository.findById(dto.getAluno())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        saldoNovo.setAluno(saldoUsuario);

        Servico servicoSaldo = servicoRepository.findById(dto.getServico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
        saldoNovo.setServico(servicoSaldo);

        saldoNovo.setQuantidade(dto.getQuantidade());

        saldoNovo = saldoRepository.save(saldoNovo);

        return toSaldoResponse(saldoNovo);
    }

    public List<SaldoResponse> listarSaldos() {
        return saldoRepository.findAll()
                .stream()
                .map(this::toSaldoResponse)
                .toList();
    }

    public SaldoResponse listarSaldoPorId(Long id) {
        Saldo saldo = saldoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo não encontrado"));

        return toSaldoResponse(saldo);
    }

    public SaldoResponse atualizarSaldoPorId(SaldoRequest dto, Long id) {
        Saldo saldoNovo = saldoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo não encontrado"));

        Usuario saldoUsuario = usuarioRepository.findById(dto.getAluno())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        saldoNovo.setAluno(saldoUsuario);

        Servico servicoSaldo = servicoRepository.findById(dto.getServico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
        saldoNovo.setServico(servicoSaldo);

        saldoNovo.setQuantidade(dto.getQuantidade());

        saldoNovo = saldoRepository.save(saldoNovo);

        return toSaldoResponse(saldoNovo);
    }

    public void deletarSaldoPorId(Long id) {
        if (!saldoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo não existe!");
        }
        saldoRepository.deleteById(id);
    }

    public ProfessorSaldoResponse buscarSaldoProfessorPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (!usuario.getTipoUsuario().getCargo().equalsIgnoreCase("professor")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não é um professor");
        }

        List<Servico> servicos = servicoRepository.findAll();

        List<ServicoProfessorResponse> listaServicos = servicos.stream().map(servico -> {

            Long aulasProfessor = agendamentoRepository
                    .countByProfessorIdAndServicoIdAndStatus(id, servico.getId(), "concluido");

            Long aulasAuxiliar = agendamentoRepository
                    .countByAuxiliarIdAndServicoIdAndStatus(id, servico.getId(), "concluido");

            return new ServicoProfessorResponse(
                    servico.getNome().toLowerCase(),
                    aulasProfessor.intValue(),
                    aulasAuxiliar.intValue()
            );

        }).toList();

        int totalProfessor = listaServicos.stream().mapToInt(ServicoProfessorResponse::getAulasProfessor).sum();
        int totalAuxiliar = listaServicos.stream().mapToInt(ServicoProfessorResponse::getAulasAuxiliar).sum();

        return new ProfessorSaldoResponse(
                new ProfessorResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        usuario.getCriadoEm()
                ),
                listaServicos,
                totalProfessor,
                totalAuxiliar
        );
    }
}