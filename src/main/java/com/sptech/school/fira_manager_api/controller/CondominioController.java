package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.Condominio;
import com.sptech.school.fira_manager_api.service.CondominioService;
import org.apache.coyote.Response;
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
@RequestMapping("/api/condominios")
public class CondominioController {

    private final CondominioService condominioService;

    public CondominioController(CondominioService condominioService) {
        this.condominioService = condominioService;
    }

    @PostMapping()
    public ResponseEntity<String> adicionarCondominio(@RequestBody Condominio condominio) {

        ResponseEntity<String> validacao = validarCondominio(condominio);
        if (validacao != null) return validacao;

        Boolean resposta = condominioService.adicionarNovoCondominio(
                condominio.getId(),
                condominio.getCidade(),
                condominio.getBairro(),
                condominio.getRua(),
                condominio.getNumero());

        if (resposta.equals(true))
            return ResponseEntity.status(201).body("Condominio adicionado com sucesso!");

        return ResponseEntity.status(400).body("Erro ao adicionar condominio.");
    }

    @GetMapping
    public ResponseEntity<List<Condominio>> buscarCondominio() {
        return ResponseEntity.status(200).body(condominioService.obterCondominios());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCondominio(@PathVariable Integer id, @RequestBody Condominio condominio) {
        ResponseEntity<String> validacao = validarCondominio(condominio);
        if (validacao != null) return validacao;

        Boolean atualizado = condominioService.atualizarCondominio(
                id,
                condominio.getCidade(),
                condominio.getBairro(),
                condominio.getRua(),
                condominio.getNumero()
        );

        if (atualizado.equals(true)) {
            return ResponseEntity.status(200).body("Condominio atualizado com sucesso!");
        }

        return ResponseEntity.status(404).body("Nenhum condominio encontrado.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarCondominio(@PathVariable Integer id) {
        Boolean deletado = condominioService.deletarCondominio(id);

        if (deletado.equals(true)) {
            return ResponseEntity.status(204).body("Condominio deletado com sucesso!");
        }

        return ResponseEntity.status(404).body("Nenhum condominio encontrado.");
    }

    private ResponseEntity<String> validarCondominio(Condominio condominio) {
        if (condominio.getCidade() == null) return ResponseEntity.status(400).body("Cidade inválido");
        if (condominio.getBairro() == null) return ResponseEntity.status(400).body("Bairro inválido");
        if (condominio.getRua() == null) return ResponseEntity.status(400).body("Rua inválida");
        if (condominio.getNumero() == null) return ResponseEntity.status(400).body("Numero inválido");

        return null;
    }
}
