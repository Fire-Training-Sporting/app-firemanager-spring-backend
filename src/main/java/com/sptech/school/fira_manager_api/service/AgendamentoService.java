package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.Agendamento;
import com.sptech.school.fira_manager_api.model.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AgendamentoService {

    private final List<Agendamento> agendamentos = new ArrayList<>();
    private final SaldoService saldoService;

    public AgendamentoService(SaldoService saldoService) {
        this.saldoService = saldoService;
    }

    public Boolean criarNovoAgendamento(Agendamento agendamento) {

        if (agendamento.getData().isBefore(LocalDateTime.now())) return false;

        Saldo saldo = saldoService.buscarPorAlunoId(
                agendamento.getAlunoId()
        );

        if (saldo == null || saldo.getQuanidade() <= 0) {
            return false;
        }

        saldo.setQuanidade(saldo.getQuanidade() - 1);

        agendamentos.add(agendamento);

        return true;
    }

    public List<Agendamento> obterAgendamentos() {
        return agendamentos;
    }

    public Boolean atualizarAgendamento(Integer id, Agendamento novo) {

        for (int i = 0; i < agendamentos.size(); i++) {

            if (Objects.equals(agendamentos.get(i).getId(), id)) {
                
                Saldo saldo = saldoService.buscarPorAlunoId(novo.getAlunoId());

                agendamentos.set(i, novo);
                return true;
            }
        }

        return false;
    }

    public Boolean cancelarAgendamento(Integer id) {

        for (int i = 0; i < agendamentos.size(); i++) {

            Agendamento agendamento = agendamentos.get(i);

            if (Objects.equals(agendamento.getId(), id)) {

                LocalDateTime agora = LocalDateTime.now();
                LocalDateTime dataAula = agendamento.getData();

                long horas = Duration.between(agora, dataAula).toHours();

                Saldo saldo = saldoService.buscarPorAlunoId(
                        agendamento.getAlunoId()
                );

                if (horas >= 24) {
                    saldo.setQuanidade(saldo.getQuanidade() + 1);
                }

                agendamentos.remove(i);
                return true;
            }
        }

        return false;
    }
}