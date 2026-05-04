package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.ServicoDTO;
import com.sptech.school.fira_manager_api.dto.responses.ServicoResponse;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.service.ServicoService;
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
@RequestMapping("/api/servicos")
@Tag(name = "Serviços", description = "Gerenciamento de serviços — Somente administradores podem criar, atualizar ou deletar")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicosService) {
        this.servicoService = servicosService;
    }

    @Operation(summary = "Lista todos os serviços", description = "Retorna todos os serviços cadastrados")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de serviços retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ServicoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<Servico>> buscarServicos() {
        return ResponseEntity.ok(servicoService.buscarServicos());
    }

    @Operation(summary = "Busca serviço por ID", description = "Retorna os dados de um serviço específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço encontrado",
                    content = @Content(schema = @Schema(implementation = ServicoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Serviço não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarServicosPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicoService.buscarServicoPorId(id));
    }

    @Operation(summary = "Atualiza disponibilidade do serviço por ID", description = "Atualiza se o serviço está disponível ou não")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ServicoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Serviço não encontrado",
                    content = @Content
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Servico> atualizarAtividadePorId(@PathVariable Long id, @Valid @RequestBody ServicoDTO dto) {
        return ResponseEntity.ok(servicoService.atualizarAtividadePorId(id, dto.getAtivo()));
    }

    @Operation(summary = "Deleta serviço por ID", description = "Deleta um serviço do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Serviço deletado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Serviço não encontrado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServicoPorId(@PathVariable Long id) {
        servicoService.deletarServicoPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
