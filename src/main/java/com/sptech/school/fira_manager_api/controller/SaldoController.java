package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.SaldoDTO;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.service.SaldoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saldos")
@Tag(name = "Saldos", description = "Gerenciamento de saldo dos alunos")
public class SaldoController {

    private final SaldoService service;

    public SaldoController(SaldoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Adicionar saldo", description = "Adiciona saldo para um aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Saldo adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao adicionar saldo")
    })
    public ResponseEntity<Saldo> adicionar(@RequestBody SaldoDTO dto) {

        Saldo saldo = service.adicionarSaldo(dto.getAlunoId(), dto.getQuantidade());

        if (saldo != null) {
            return ResponseEntity.status(201).body(saldo);
        }

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{alunoId}/remover")
    @Operation(summary = "Remover saldo", description = "Remove uma quantidade do saldo do aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao remover saldo")
    })
    public ResponseEntity<Saldo> remover(@PathVariable Long alunoId,
                                         @RequestBody SaldoDTO dto) {

        Saldo saldo = service.removerSaldo(alunoId, dto.getQuantidade());

        if (saldo != null) {
            return ResponseEntity.ok(saldo);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    @Operation(summary = "Listar saldos", description = "Retorna todos os saldos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de saldos retornada com sucesso")
    public ResponseEntity<List<Saldo>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{alunoId}")
    @Operation(summary = "Buscar saldo por aluno", description = "Retorna o saldo de um aluno específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo encontrado"),
            @ApiResponse(responseCode = "404", description = "Saldo não encontrado")
    })
    public ResponseEntity<Saldo> buscar(@PathVariable Long alunoId) {

        Saldo saldo = service.buscarPorAlunoId(alunoId);

        if (saldo != null) {
            return ResponseEntity.ok(saldo);
        }

        return ResponseEntity.notFound().build();
    }
}