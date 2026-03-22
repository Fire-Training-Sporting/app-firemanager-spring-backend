package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.Agendamento;
import com.sptech.school.fira_manager_api.dto.Servico;
import com.sptech.school.fira_manager_api.service.ServicosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicosService servicosService;

    public ServicoController(ServicosService servicosService) {
        this.servicosService = servicosService;
    }

    @PostMapping()
    public ResponseEntity<Servico> criarServico(@RequestBody Servico servico) {

        if (servico.getNome().isBlank() || servico.getNome().isEmpty()) return ResponseEntity.status(400).build();

        Servico criado = servicosService.adicionarServico(servico.getNome());
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping()
    public ResponseEntity<List<Servico>> buscarServicos() {
        return ResponseEntity.status(200).body(servicosService.obterServicos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @RequestBody Servico novoNome) {

        if (id == null) return ResponseEntity.status(400).build();

        Servico atualizado = servicosService.atualizarServico(id, novoNome);

        if (atualizado == null) return ResponseEntity.status(404).build();

        return ResponseEntity.status(200).body(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletarServico(@PathVariable Long id) {

        if (id == null) return ResponseEntity.status(400).build();

        Boolean deletado = servicosService.deletarServico(id);

        if (deletado) return ResponseEntity.status(204).body(deletado);
        return ResponseEntity.status(404).body(deletado);
    }
}
