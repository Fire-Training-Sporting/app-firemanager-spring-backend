package com.sptech.school.fira_manager_api.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoRequest;
import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoRecorrenteRequest;
import com.sptech.school.fira_manager_api.dto.requests.agendamento.AgendamentoStatusRequest;
import com.sptech.school.fira_manager_api.dto.responses.agendamento.AgendamentoResponse;
import com.sptech.school.fira_manager_api.service.AgendamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos dos alunos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @Operation(summary = "Cria um novo agendamento", description = "Cria um agendamento para um aluno com professor, serviço e data especificada")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Agendamento criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Aluno, Professor, Auxiliar, Serviço ou Condomínio não encontrado",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<AgendamentoResponse> criarAgendamento(@Valid @RequestBody AgendamentoRequest dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criarAgendamento(dto));
    }

    @Operation(summary = "Cria agendamentos recorrentes")
    @ApiResponses(value = {
    @ApiResponse(
                responseCode = "201",
                description = "Agendamentos recorrentes criados com sucesso",
                content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                        schema = @Schema(implementation = AgendamentoResponse.class)
                )
                )
        )
    })
    @PostMapping("/recorrente")
    public ResponseEntity<List<AgendamentoResponse>> criarAgendamentoRecorrente(@Valid @RequestBody AgendamentoRecorrenteRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criarAgendamentoRecorrente(dto));
    }

    @Operation(summary = "Lista todos os agendamentos", description = "Retorna todos os agendamentos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de agendamentos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarAgendamentos(@RequestParam (required = false) String status){

        if (status != null) {
            return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.buscarAgendamentoPorStatus(status));
        }
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.listarAgendamento());
    }


    @Operation(summary = "Busca agendamento por ID", description = "Retorna os dados de um agendamento específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento encontrado",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> listarAgendamentosPorId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.listarAgendamentoPorId(id));
    }

    @Operation(summary = "Atualiza agendamento por ID", description = "Atualiza os dados de um agendamento existente. Apenas agendamentos com status 'pendente' podem ser atualizados")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "O agendamento não pode ser atualizado (status diferente de 'pendente')",
                    content = @Content
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizarAgendamentoPorId(@Valid @RequestBody AgendamentoRequest dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.atualizarAgendamentoPorId(dto, id));
    }

    @Operation(summary = "Atualiza status do agendamento", description = "Atualiza o status de um agendamento existente (ex: pendente, confirmado, cancelado)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status do agendamento atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida ou status vazio",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content
            )
    })
    @PatchMapping("/status/{id}")
    public ResponseEntity<AgendamentoResponse> atualizarStatusAgendamentoPorId(@PathVariable Long id, @Valid @RequestBody AgendamentoStatusRequest dto){
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.atualizarStatusAgendamentoPorId(id, dto));
    }

    @Operation(summary = "Deleta agendamento por ID", description = "Deleta um agendamento do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Agendamento deletado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamentoPorId(@PathVariable Long id){
        agendamentoService.deletarAgendamentoPorId(id);
        return ResponseEntity.noContent().build();
    }
}