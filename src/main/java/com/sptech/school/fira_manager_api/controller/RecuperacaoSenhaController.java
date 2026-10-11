package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.requests.usuario.RedefinirSenhaRequest;
import com.sptech.school.fira_manager_api.dto.requests.usuario.SolicitarRecuperacaoSenhaRequest;
import com.sptech.school.fira_manager_api.service.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recuperacao-senha")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public RecuperacaoSenhaController(RecuperacaoSenhaService recuperacaoSenhaService) {
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @PostMapping
    public ResponseEntity<Void> solicitarRecuperacao(
            @Valid @RequestBody SolicitarRecuperacaoSenhaRequest request) {
        recuperacaoSenhaService.solicitarRecuperacao(request.getEmail());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/redefinir")
    public ResponseEntity<Void> redefinirSenha(@Valid @RequestBody RedefinirSenhaRequest request) {
        recuperacaoSenhaService.redefinirSenha(request.getToken(), request.getNovaSenha());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
