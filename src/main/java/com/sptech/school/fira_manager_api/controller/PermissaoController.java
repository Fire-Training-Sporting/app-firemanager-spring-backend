package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.PermissaoDTO;
import com.sptech.school.fira_manager_api.dto.responses.PermissaoResponse;
import com.sptech.school.fira_manager_api.model.Permissao;
import com.sptech.school.fira_manager_api.service.PermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissoes")
@Tag(name = "Permissões", description = "Gerenciamento de permissões — Somente administradores podem criar, atualizar ou deletar")
public class PermissaoController {

    private final PermissaoService permissaoService;

    public PermissaoController(PermissaoService permissaoService) {
        this.permissaoService = permissaoService;
    }

    @Operation(summary = "Cria uma nova permissão", description = "Cria uma nova permissão no sistema. Somente administradores podem acessar este endpoint")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Permissão criada com sucesso",
                    content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<Permissao> adicionarPermissao(@Valid @RequestBody PermissaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissaoService.adicionarNovaPermissao(dto));
    }

    @Operation(summary = "Lista todas as permissões cadastradas", description = "Retorna todas as permissões cadastrados")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de permissões retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<Permissao>> buscarPermissoes() {
        return ResponseEntity.ok(permissaoService.obterPermissoes());
    }

    @Operation(summary = "Atualiza permissão por ID", description = "Atualiza os dados de uma permissão existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Permissão atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = PermissaoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Permissão não encontrada",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Permissao> atualizarPermissao(@PathVariable Long id, @Valid @RequestBody PermissaoDTO dto) {
        return ResponseEntity.ok(permissaoService.atualizarPermissao(id, dto));
    }

    @Operation(summary = "Deleta permissão por ID", description = "Deleta uma permissão do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Permissão a com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Permissão não encontrada",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPermissao(@PathVariable Long id) {
        permissaoService.deletarPermissao(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}