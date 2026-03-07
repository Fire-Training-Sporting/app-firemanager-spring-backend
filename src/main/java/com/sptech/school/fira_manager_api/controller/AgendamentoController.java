package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.Agendamento;
import com.sptech.school.fira_manager_api.service.AgendamentoService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping()
    public ResponseEntity<String> criarAgendamento(@RequestBody Agendamento agendamento) {

        ResponseEntity<String> validacao = validarAgendamento(agendamento);
        if (validacao != null) return validacao;

        Boolean resposta = agendamentoService.criarNovoAgendamento(
                agendamento.getId(),
                agendamento.getAluno(),
                agendamento.getProfessor(),
                agendamento.getData(),
                agendamento.getLocal(),
                agendamento.getSaldo(),
                agendamento.getServico(),
                agendamento.getObservacao());

        if (resposta.equals(true))
            return ResponseEntity.status(201).body("Agendamento criado com sucesso!");

        return ResponseEntity.status(400).body("Erro ao criar agendamento");
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> buscarAgendamento() {
        return ResponseEntity.status(200).body(agendamentoService.obterAgendamentos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarAgendamento(@PathVariable Integer id, @RequestBody Agendamento agendamento) {
        ResponseEntity<String> validacao = validarAgendamento(agendamento);
        if (validacao != null) return validacao;

        Boolean atualizado = agendamentoService.atualizarAgendamento(
                id,
                agendamento.getAluno(),
                agendamento.getProfessor(),
                agendamento.getData(),
                agendamento.getLocal(),
                agendamento.getSaldo(),
                agendamento.getServico(),
                agendamento.getObservacao()
        );

        if (atualizado.equals(true)) {
            return ResponseEntity.status(200).body("Agendamento atualizado com sucesso!");
        }

        return ResponseEntity.status(404).body("Nenhum agendamento encontrado para atualizar");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarAgendamento(@PathVariable Integer id) {
        Boolean deletado = agendamentoService.deletarAgendamento(id);

        if (deletado.equals(true)) {
            return ResponseEntity.status(204).body("Agendamento deletado com sucesso!");
        }

        return ResponseEntity.status(404).body("Nenhum agendamento encontrado para deletar");
    }

    private ResponseEntity<String> validarAgendamento(Agendamento agendamento) {
        if (agendamento.getAluno() == null) return ResponseEntity.status(400).body("Aluno inválido");
        if (agendamento.getData() == null) return ResponseEntity.status(400).body("Data inválida");
        if (agendamento.getProfessor() == null) return ResponseEntity.status(400).body("Professor inválido");
        if (agendamento.getSaldo() == null) return ResponseEntity.status(400).body("Saldo inválido");
        if (agendamento.getLocal() == null) return ResponseEntity.status(400).body("Local inválido");
        if (agendamento.getServico() == null) return ResponseEntity.status(400).body("Serviço inválido");

        return null;
    }
}