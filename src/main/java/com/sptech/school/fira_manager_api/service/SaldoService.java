package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.SaldoDTO;
import com.sptech.school.fira_manager_api.dto.responses.*;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.AgendamentoRepository;
import com.sptech.school.fira_manager_api.repository.SaldoRepository;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    private ProfessorSaldoResponse toProfessorSaldoResponse(Usuario usuario, Long aulasComoProfessor, Long aulasComoAuxiliar) {
        if (usuario == null) {
            return null;
        }

        return new ProfessorSaldoResponse(
                new ProfessorResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        usuario.getCriadoEm()
                ),
                aulasComoProfessor.intValue(),
                aulasComoAuxiliar.intValue()
        );
    }

    public SaldoResponse criarSaldo(SaldoDTO dto) {
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

    public SaldoResponse atualizarSaldoPorId(SaldoDTO dto, Long id) {
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

        String tipoUsuario = usuario.getTipoUsuario().getCargo().toLowerCase();

        if (!tipoUsuario.equals("professor")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não é um professor");
        }

        Long comoProfessor = agendamentoRepository
                .countByProfessorIdAndStatus(id, "concluida");

        Long comoAuxiliar = agendamentoRepository
                .countByAuxiliarIdAndStatus(id, "concluida");

        return toProfessorSaldoResponse(usuario, comoProfessor, comoAuxiliar);
    }
}