package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.AgendamentoDTO;
import com.sptech.school.fira_manager_api.model.Saldo;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AgendamentoService {

    private final List<AgendamentoDTO> agendamentos = new ArrayList<>();
    private final Map<Long, AgendamentoDTO> mapa = new HashMap<>();
    private Long contadorId = 1L;

    private final SaldoService saldoService;

    public AgendamentoService(SaldoService saldoService) {
        this.saldoService = saldoService;
    }

    public Boolean criarNovoAgendamento(AgendamentoDTO dto) {

        if (dto.getData().isBefore(LocalDateTime.now())) return false;

        Saldo saldo = saldoService.buscarPorAlunoId(dto.getAlunoId());

        if (saldo == null || saldo.getQuantidade() <= 0) return false;

        saldo.setQuantidade(saldo.getQuantidade() - 1);

        mapa.put(contadorId++, dto);
        agendamentos.add(dto);

        return true;
    }

    public List<AgendamentoDTO> obterAgendamentos() {
        return agendamentos;
    }

    public Boolean atualizarAgendamento(Long id, AgendamentoDTO novo) {

        if (!mapa.containsKey(id)) return false;

        if (novo.getData().isBefore(LocalDateTime.now())) return false;

        mapa.put(id, novo);

        return true;
    }

    public Boolean cancelarAgendamento(Long id) {

        AgendamentoDTO dto = mapa.get(id);

        if (dto == null) return false;

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataAula = dto.getData();

        long horas = Duration.between(agora, dataAula).toHours();

        Saldo saldo = saldoService.buscarPorAlunoId(dto.getAlunoId());

        if (saldo != null && horas >= 24) {
            saldo.setQuantidade(saldo.getQuantidade() + 1);
        }

        mapa.remove(id);
        agendamentos.remove(dto);

        return true;
    }
}