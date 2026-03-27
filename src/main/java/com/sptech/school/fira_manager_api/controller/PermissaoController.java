package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.Permissao;
import com.sptech.school.fira_manager_api.service.PermissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissoes")
public class PermissaoController {

    private final PermissaoService permissaoService;

    public PermissaoController(PermissaoService permissaoService) {
        this.permissaoService = permissaoService;
    }

    @PostMapping
    public ResponseEntity<String> adicionarPermissao(@RequestBody Permissao permissao) {

        ResponseEntity<String> validacao = validarPermissao(permissao);
        if (validacao != null) return validacao;

        Boolean resposta = permissaoService.adicionarNovaPermissao(
                permissao.getId(),
                permissao.getNome()
        );

        if (resposta.equals(true)) {
            return ResponseEntity.status(201).body("Permissão adicionada com sucesso!");
        }

        return ResponseEntity.status(400).body("Erro ao adicionar permissão.");
    }

    @GetMapping
    public ResponseEntity<List<Permissao>> buscarPermissoes() {
        return ResponseEntity.status(200).body(permissaoService.obterPermissoes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPermissao(@PathVariable Integer id, @RequestBody Permissao permissao) {

        ResponseEntity<String> validacao = validarPermissao(permissao);
        if (validacao != null) return validacao;

        Boolean atualizado = permissaoService.atualizarPermissao(
                id,
                permissao.getNome()
        );

        if (atualizado.equals(true)) {
            return ResponseEntity.status(200).body("Permissão atualizada com sucesso!");
        }

        return ResponseEntity.status(404).body("Nenhuma permissão encontrada.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPermissao(@PathVariable Integer id) {
        Boolean deletado = permissaoService.deletarPermissao(id);

        if (deletado.equals(true)) {
            return ResponseEntity.status(204).body("Permissão deletada com sucesso!");
        }

        return ResponseEntity.status(404).body("Nenhuma permissão encontrada.");
    }

    private ResponseEntity<String> validarPermissao(Permissao permissao) {
        if (permissao.getId() == null) return ResponseEntity.status(400).body("Id inválido");
        if (permissao.getNome() == null || permissao.getNome().isBlank()) {
            return ResponseEntity.status(400).body("Nome inválido");
        }

        return null;
    }
}