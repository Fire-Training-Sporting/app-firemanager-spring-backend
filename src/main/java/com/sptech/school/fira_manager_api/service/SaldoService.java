package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.SaldoDTO;
import com.sptech.school.fira_manager_api.dto.responses.SaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.ServicoResponse;
import com.sptech.school.fira_manager_api.dto.responses.UsuarioResponse;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.Usuario;
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

    public SaldoService(SaldoRepository saldoRepository, UsuarioRepository usuarioRepository, ServicoRepository servicoRepository) {
        this.saldoRepository = saldoRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicoRepository = servicoRepository;
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
}