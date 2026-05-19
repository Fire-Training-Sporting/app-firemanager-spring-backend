package com.sptech.school.fira_manager_api.service;

import java.time.LocalDate;
import java.util.List;

import com.sptech.school.fira_manager_api.dto.responses.saldoTransacoes.SaldoTransacaoResponse;
import com.sptech.school.fira_manager_api.model.SaldoTransacao;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.sptech.school.fira_manager_api.repository.SaldoTransacaoRepository;
import com.sptech.school.fira_manager_api.repository.ServicoRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;

@Service
public class SaldoService {

    private final SaldoRepository saldoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicoRepository servicoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final SaldoTransacaoRepository saldoTransacaoRepository;

    public SaldoService(SaldoRepository saldoRepository, UsuarioRepository usuarioRepository, ServicoRepository servicoRepository, AgendamentoRepository agendamentoRepository, SaldoTransacaoRepository saldoTransacaoRepository) {
        this.saldoRepository = saldoRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicoRepository = servicoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.saldoTransacaoRepository = saldoTransacaoRepository;
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

    private SaldoTransacaoResponse toSaldoTransacaoResponse(SaldoTransacao t) {
        return new SaldoTransacaoResponse(
                t.getId(),
                t.getQuantidadeOriginal(),
                t.getQuantidadeRestante(),
                t.getDataExpiracao(),
                t.getCriadoEm()
        );
    }


    private SaldoResponse toSaldoResponse(Saldo saldo) {
        if (saldo == null) return null;

        List<SaldoTransacaoResponse> lotes = saldoTransacaoRepository
                .findBySaldoIdAndQuantidadeRestanteGreaterThanAndDataExpiracaoGreaterThanEqualOrderByDataExpiracaoAsc(
                        saldo.getId(), 0.0, LocalDate.now())
                .stream()
                .map(this::toSaldoTransacaoResponse)
                .toList();

        return new SaldoResponse(
                saldo.getId(),
                toUsuarioResponse(saldo.getAluno()),
                saldo.getQuantidade(),
                toServicoResponse(saldo.getServico()),
                lotes
        );
    }

    private void adicionarLote(Saldo saldo, Double quantidade) {
        LocalDate hoje = LocalDate.now();
        SaldoTransacao transacao = new SaldoTransacao(saldo, quantidade, hoje.plusMonths(3), hoje);
        saldoTransacaoRepository.save(transacao);
        saldo.setQuantidade(saldo.getQuantidade() + quantidade);
        saldoRepository.save(saldo);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expirarSaldos() {
        List<SaldoTransacao> expiradas = saldoTransacaoRepository
                .findByDataExpiracaoBeforeAndQuantidadeRestanteGreaterThan(LocalDate.now(), 0.0);

        for (SaldoTransacao transacao : expiradas) {
            Saldo saldo = transacao.getSaldo();
            saldo.setQuantidade(Math.max(0.0, saldo.getQuantidade() - transacao.getQuantidadeRestante()));
            transacao.setQuantidadeRestante(0.0);
            saldoTransacaoRepository.save(transacao);
            saldoRepository.save(saldo);
        }
    }

    public SaldoResponse criarSaldo(SaldoRequest dto) {
        Usuario saldoUsuario = usuarioRepository.findById(dto.getAluno())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));

        Servico servicoSaldo = servicoRepository.findById(dto.getServico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        Saldo saldo = saldoRepository.findByAlunoIdAndServicoId(dto.getAluno(), dto.getServico())
                .orElseGet(() -> {
                    Saldo novo = new Saldo();
                    novo.setAluno(saldoUsuario);
                    novo.setServico(servicoSaldo);
                    novo.setQuantidade(0.0);
                    return saldoRepository.save(novo);
                });

        adicionarLote(saldo, dto.getQuantidade());

        return toSaldoResponse(saldo);
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
        Saldo saldo = saldoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo não encontrado"));

        usuarioRepository.findById(dto.getAluno())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        servicoRepository.findById(dto.getServico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        adicionarLote(saldo, dto.getQuantidade());

        return toSaldoResponse(saldo);
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
                    .countByProfessorIdAndServicoIdAndStatus(id, servico.getId(), "finalizado");

            Long aulasAuxiliar = agendamentoRepository
                    .countByAuxiliarIdAndServicoIdAndStatus(id, servico.getId(), "finalizado");

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