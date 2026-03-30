package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.repository.SaldoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaldoService {

    private final SaldoRepository repository;

    public SaldoService(SaldoRepository repository) {
        this.repository = repository;
    }

    public List<Saldo> listar() {
        return repository.findAll();
    }

    public Saldo buscarPorAlunoId(Long alunoId) {
        return repository.findAll()
                .stream()
                .filter(s -> s.getAlunoId().equals(alunoId))
                .findFirst()
                .orElse(null);
    }

    public Saldo adicionarSaldo(Long alunoId, Integer valor) {

        if (alunoId == null || valor == null || valor <= 0) return null;

        Saldo saldo = buscarPorAlunoId(alunoId);

        if (saldo == null) {
            saldo = new Saldo(alunoId, valor);
        } else {
            saldo.setQuantidade(saldo.getQuantidade() + valor);
        }

        return repository.save(saldo);
    }

    public Saldo removerSaldo(Long alunoId, Integer valor) {

        if (alunoId == null || valor == null || valor <= 0) return null;

        Saldo saldo = buscarPorAlunoId(alunoId);

        if (saldo == null || saldo.getQuantidade() < valor) return null;

        saldo.setQuantidade(saldo.getQuantidade() - valor);

        return repository.save(saldo);
    }
}