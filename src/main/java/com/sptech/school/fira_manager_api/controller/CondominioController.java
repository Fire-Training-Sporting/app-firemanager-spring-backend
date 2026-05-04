package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.CondominioDTO;
import com.sptech.school.fira_manager_api.dto.responses.CondominioResponse;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.service.CondominioService;
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
@RequestMapping("/api/condominios")
@Tag(name = "Condominios", description = "Gerenciamento de condominios — Somente funcionários podem criar, atualizar ou deletar")
public class CondominioController {

    private final CondominioService condominioService;

    public CondominioController(CondominioService condominioService) {
        this.condominioService = condominioService;
    }

    @Operation(summary = "Cria um novo condominio", description = "Cria um condominio no sistema. Somente funcionários podem acessar este endpoint")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Condominio criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CondominioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            )
    })
    @PostMapping()
    public ResponseEntity<Condominio> adicionarCondominio(@Valid @RequestBody CondominioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(condominioService.adicionarNovoCondominio(dto));
    }

    @Operation(summary = "Lista todos os condominios cadastrados", description = "Retorna todos os condominios cadastrados")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de condominios retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = CondominioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<Condominio>> buscarCondominio() {
        return ResponseEntity.ok(condominioService.obterCondominios());
    }

    @Operation(summary = "Atualiza condominio por ID", description = "Atualiza os dados de um condominio existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Condominio atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = CondominioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Condominio não encontrado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Condominio> atualizarCondominio(@PathVariable Long id, @Valid @RequestBody CondominioDTO dto) {
        return ResponseEntity.ok(condominioService.atualizarCondominio(id, dto));
    }

    @Operation(summary = "Deleta condominio por ID", description = "Deleta um condominio do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Condominio deletado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Condominio não encontrado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCondominio(@PathVariable Long id) {
        condominioService.deletarCondominio(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
