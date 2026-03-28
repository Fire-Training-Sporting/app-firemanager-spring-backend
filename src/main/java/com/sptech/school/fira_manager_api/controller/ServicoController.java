package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.ServicoDTO;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicosService) {
        this.servicoService = servicosService;
    }

    @PostMapping()
    public ResponseEntity<Servico> criarServico(@RequestBody ServicoDTO dto) {
        Servico servicoNovo = new Servico(dto.getNome());
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criarServico(servicoNovo));
    }

    @GetMapping()
    public ResponseEntity<List<Servico>> buscarServicos() {
        return ResponseEntity.ok(servicoService.buscarServicos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizarServico(@PathVariable Long id, @RequestBody ServicoDTO novoNome) {

        if (id == null) return ResponseEntity.status(400).build();

        ServicoDTO atualizado = servicoService.atualizarServico(id, novoNome);

        if (atualizado == null) return ResponseEntity.status(404).build();

        return ResponseEntity.status(200).body(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletarServico(@PathVariable Long id) {

        if (id == null) return ResponseEntity.status(400).build();

        Boolean deletado = servicoService.deletarServico(id);

        if (deletado) return ResponseEntity.status(204).body(deletado);
        return ResponseEntity.status(404).body(deletado);
    }
}
