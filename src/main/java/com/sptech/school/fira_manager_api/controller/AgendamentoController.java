package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.Agendamento;
import com.sptech.school.fira_manager_api.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody Agendamento agendamento) {

        if (service.criarNovoAgendamento(agendamento) == null) {
            return ResponseEntity.status(201).body("Agendamento criado!");
        }

        return ResponseEntity.badRequest().body("Erro ao criar agendamento");
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listar() {
        return ResponseEntity.ok(service.obterAgendamentos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Integer id, @RequestBody Agendamento agendamento) {

        if (service.atualizarAgendamento(id, agendamento)) {
            return ResponseEntity.ok("Atualizado com sucesso");
        }

        return ResponseEntity.status(404).body("Agendamento não encontrado");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelar(@PathVariable Integer id) {

        if (service.cancelarAgendamento(id)) {
            return ResponseEntity.ok("Cancelado com sucesso");
        }

        return ResponseEntity.status(404).body("Agendamento não encontrado");
    }
}