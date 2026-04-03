package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.AgendamentoDTO;
import com.sptech.school.fira_manager_api.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Cria um novo agendamento", description = "Cria um novo agendamento no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou dados inconsistentes"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar agendamento")
    })
    @PostMapping
    public ResponseEntity<AgendamentoDTO> criar(@Valid @RequestBody AgendamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(dto));
    }

    @Operation(summary = "Lista todos os agendamentos", description = "Retorna uma lista com todos os agendamentos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso",
            content = @Content(schema = @Schema(implementation = AgendamentoDTO.class)))
    @GetMapping
    public ResponseEntity<List<AgendamentoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Atualiza um agendamento", description = "Atualiza os dados de um agendamento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou dados inconsistentes"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar agendamento")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody AgendamentoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Remove um agendamento", description = "Deleta um agendamento do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agendamento removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao remover agendamento")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}