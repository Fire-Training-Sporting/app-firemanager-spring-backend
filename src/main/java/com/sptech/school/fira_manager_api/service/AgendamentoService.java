package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.*;
import com.sptech.school.fira_manager_api.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AgendamentoService {

    private final List<Agendamento> agendamentos = new ArrayList<>();

    public Boolean criarNovoAgendamento(
            Integer id,
            Aluno aluno,
            Professor professor,
            LocalDateTime data,
            Local local,
            Saldo saldo,
            Servico servico,
            String observacao) {
        Agendamento novoAgendamento = new Agendamento(
                id,
                aluno,
                professor,
                data,
                local,
                saldo,
                servico,
                observacao);
        agendamentos.add(novoAgendamento);

        return true;
    }

    public List<Agendamento> obterAgendamentos() {
        return agendamentos;
    }

    public Boolean atualizarAgendamento(
            Integer id,
            Aluno aluno,
            Professor professor,
            LocalDateTime data,
            Local local,
            Saldo saldo,
            Servico servico,
            String observacao) {
        for (int i = 0; i < agendamentos.size(); i++) {
            Agendamento agendamentoAtual = agendamentos.get(i);

            if (Objects.equals(agendamentoAtual.getId(), id)) {
                agendamentos.set(i, new Agendamento(
                        id,
                        aluno,
                        professor,
                        data,
                        local,
                        saldo,
                        servico,
                        observacao));
                return true;
            }
        }

        return false;
    }

    public Boolean deletarAgendamento(Integer id) {
        for (int i = 0; i < agendamentos.size(); i++) {
            if (Objects.equals(agendamentos.get(i).getId(), id)) {
                agendamentos.remove(i);
                return true;
            }
        }

        return false;
    }
}