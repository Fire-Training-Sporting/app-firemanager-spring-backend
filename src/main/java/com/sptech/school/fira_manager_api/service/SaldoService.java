package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.dto.ServicoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaldoService {

    private final List<Saldo> saldos = new ArrayList<>();

    public List<Saldo> listar() {
        return saldos;
    }

    public Saldo buscarPorAlunoId(Long alunoId) {
        for (Saldo saldo : saldos) {
            if (saldo.getAlunoID().equals(alunoId)) {
                return saldo;
            }
        }
        return null;
    }

    public Saldo adicionarSaldo(Long alunoId, Integer valor, ServicoDTO servico) {
        if (alunoId == null || valor == null || valor <= 0) {
            return null;
        }

        Saldo saldoExistente = buscarPorAlunoId(alunoId);

        if (saldoExistente == null) {
            Saldo novoSaldo = new Saldo(alunoId, valor, servico);
            saldos.add(novoSaldo);
            return novoSaldo;
        }

        int novoValor = saldoExistente.getQuanidade() + valor;
        saldoExistente.setQuanidade(novoValor);

        if (servico != null) {
            saldoExistente.setServico(servico);
        }

        return saldoExistente;
    }

    public Saldo atualizarSaldo(Long alunoId, Integer valor, ServicoDTO servico) {
        if (alunoId == null || valor == null || valor < 0) {
            return null;
        }

        Saldo saldoExistente = buscarPorAlunoId(alunoId);

        if (saldoExistente == null) {
            return null;
        }

        saldoExistente.setQuanidade(valor);

        if (servico != null) {
            saldoExistente.setServico(servico);
        }

        return saldoExistente;
    }

    public Saldo removerSaldo(Long alunoId, Integer valor, ServicoDTO servico) {
        if (alunoId == null || valor == null || valor <= 0) {
            return null;
        }

        Saldo saldoExistente = buscarPorAlunoId(alunoId);

        if (saldoExistente == null) {
            return null;
        }

        int saldoAtual = saldoExistente.getQuanidade();
        int novoSaldo = saldoAtual - valor;

        if (novoSaldo < 0) {
            return null;
        }

        saldoExistente.setQuanidade(novoSaldo);

        if (servico != null) {
            saldoExistente.setServico(servico);
        }

        return saldoExistente;
    }
}