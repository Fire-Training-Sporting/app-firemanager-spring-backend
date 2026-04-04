package com.sptech.school.fira_manager_api.controller;
import com.sptech.school.fira_manager_api.dto.AgendamentoAtualizarDTO;
import com.sptech.school.fira_manager_api.dto.AgendamentoDTO;
import com.sptech.school.fira_manager_api.dto.AgendamentoStatusDTO;
import com.sptech.school.fira_manager_api.dto.responses.AgendamentoResponse;
import com.sptech.school.fira_manager_api.service.AgendamentoService;
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

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criarAgendamento(@Valid @RequestBody AgendamentoDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criarAgendamento(dto));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarAgendamentos(){
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.listarAgendamento());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> listarAgendamentosPorId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.listarAgendamentoPorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizarAgendamentoPorId(@Valid @RequestBody AgendamentoAtualizarDTO dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.atualizarAgendamentoPorId(dto, id));
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<AgendamentoResponse> atualizarStatusAgendamentoPorId(@PathVariable Long id, @Valid @RequestBody AgendamentoStatusDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.atualizarStatusAgendamentoPorId(id, dto));
    }

    @GetMapping("/status")
    public ResponseEntity<List<AgendamentoResponse>> buscarAgendamentoPorStatus(@RequestParam String status){
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.buscarAgendamentoPorStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamentoPorId(@PathVariable Long id){
        agendamentoService.deletarAgendamentoPorId(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}