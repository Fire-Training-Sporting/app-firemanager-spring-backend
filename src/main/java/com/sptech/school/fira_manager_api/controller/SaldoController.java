package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.requests.saldo.SaldoRequest;
import com.sptech.school.fira_manager_api.dto.responses.saldo.ProfessorSaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.saldo.SaldoResponse;
import com.sptech.school.fira_manager_api.service.SaldoService;
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
@RequestMapping("/api/saldos")
@Tag(name = "Saldos", description = "Gerenciamento de saldos dos alunos")
public class SaldoController {

    private final SaldoService saldoService;

    public SaldoController(SaldoService saldoService) {
        this.saldoService = saldoService;
    }

    @Operation(summary = "Cria um novo saldo", description = "Cria um saldo para um aluno em um serviço específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Saldo criado com sucesso",
                    content = @Content(schema = @Schema(implementation = SaldoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Aluno ou Serviço não encontrado",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<SaldoResponse> criarSaldo(@Valid @RequestBody SaldoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saldoService.criarSaldo(dto));
    }

    @Operation(summary = "Lista todos os saldos", description = "Retorna todos os saldos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de saldos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = SaldoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<SaldoResponse>> listarSaldos() {
        return ResponseEntity.status(HttpStatus.OK).body(saldoService.listarSaldos());
    }

    @Operation(summary = "Busca saldo por ID", description = "Retorna os dados de um saldo específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo encontrado",
                    content = @Content(schema = @Schema(implementation = SaldoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Saldo não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaldoResponse> listarSaldoPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(saldoService.listarSaldoPorId(id));
    }

    @Operation(summary = "Atualiza saldo por ID", description = "Atualiza os dados de um saldo existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = SaldoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Saldo não encontrado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SaldoResponse> atualizarSaldoPorId(@PathVariable Long id, @Valid @RequestBody SaldoRequest dto) {
        return ResponseEntity.status(HttpStatus.OK).body(saldoService.atualizarSaldoPorId(dto, id));
    }

    @Operation(summary = "Deleta saldo por ID", description = "Deleta um saldo do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Saldo deletado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Saldo não encontrado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSaldoPorId(@PathVariable Long id) {
        saldoService.deletarSaldoPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Busca saldo de aulas do professor por ID", description = "Retorna o total de aulas concluídas de um professor, como professor e como auxiliar")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo do professor encontrado",
                    content = @Content(schema = @Schema(implementation = ProfessorSaldoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Professor não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/professor/{id}")
    public ResponseEntity<ProfessorSaldoResponse> buscarSaldoProfessorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(saldoService.buscarSaldoProfessorPorId(id));
    }
}