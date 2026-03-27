package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.service.SaldoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saldos")
public class SaldoController {

    private final SaldoService saldoService;

    public SaldoController(SaldoService saldoService) {
        this.saldoService = saldoService;
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Saldo> adicionarSaldo(@RequestBody Saldo saldo) {
        if (saldo == null) {
            return ResponseEntity.badRequest().build();
        }

        Saldo resposta = saldoService.adicionarSaldo(
                saldo.getAlunoID(),
                saldo.getQuanidade(),
                saldo.getServico()
        );

        if (resposta != null) {
            return ResponseEntity.ok(resposta);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Saldo> atualizarSaldo(@RequestBody Saldo saldo) {
        if (saldo == null) {
            return ResponseEntity.badRequest().build();
        }

        Saldo resposta = saldoService.atualizarSaldo(
                saldo.getAlunoID(),
                saldo.getQuanidade(),
                saldo.getServico()
        );

        if (resposta != null) {
            return ResponseEntity.ok(resposta);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/remover")
    public ResponseEntity<Saldo> removerSaldo(@RequestBody Saldo saldo) {
        if (saldo == null) {
            return ResponseEntity.badRequest().build();
        }

        Saldo resposta = saldoService.removerSaldo(
                saldo.getAlunoID(),
                saldo.getQuanidade(),
                saldo.getServico()
        );

        if (resposta != null) {
            return ResponseEntity.ok(resposta);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<Saldo>> listarSaldos() {
        return ResponseEntity.ok(saldoService.listar());
    }

    @GetMapping("/{alunoId}")
    public ResponseEntity<Saldo> buscarPorAluno(@PathVariable Long alunoId) {
        Saldo saldo = saldoService.buscarPorAlunoId(alunoId);
        if (saldo != null) {
            return ResponseEntity.ok(saldo);
        }
        return ResponseEntity.notFound().build();
    }
}