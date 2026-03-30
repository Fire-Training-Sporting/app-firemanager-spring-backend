package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.AgendamentoDTO;
import com.sptech.school.fira_manager_api.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos dos alunos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao criar agendamento")
    })
    public ResponseEntity<String> criar(@RequestBody AgendamentoDTO agendamentoDTO) {

        if (service.criarNovoAgendamento(agendamentoDTO)) {
            return ResponseEntity.status(201).body("Agendamento criado!");
        }

        return ResponseEntity.badRequest().body("Erro ao criar agendamento");
    }

    @GetMapping
    @Operation(summary = "Listar agendamentos", description = "Retorna todos os agendamentos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso")
    public ResponseEntity<List<AgendamentoDTO>> listar() {
        return ResponseEntity.ok(service.obterAgendamentos());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar agendamento", description = "Atualiza os dados de um agendamento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    public ResponseEntity<String> atualizar(@PathVariable Long id,
                                            @RequestBody AgendamentoDTO agendamentoDTO) {

        if (service.atualizarAgendamento(id, agendamentoDTO)) {
            return ResponseEntity.ok("Atualizado com sucesso");
        }

        return ResponseEntity.status(404).body("Agendamento não encontrado");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar agendamento", description = "Remove (cancela) um agendamento pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    public ResponseEntity<String> cancelar(@PathVariable Long id) {

        if (service.cancelarAgendamento(id)) {
            return ResponseEntity.ok("Cancelado com sucesso");
        }

        return ResponseEntity.status(404).body("Agendamento não encontrado");
    }
}