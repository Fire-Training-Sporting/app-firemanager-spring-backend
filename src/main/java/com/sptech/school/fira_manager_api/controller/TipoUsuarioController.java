package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.service.TipoUsuarioService;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.sptech.school.fira_manager_api.dto.responses.tipoUsuario.TipoUsuarioResponse;

@RestController
@RequestMapping("api/tipo-usuarios")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    @GetMapping
    public ResponseEntity<List<TipoUsuarioResponse>> buscarTipoUsuarios() {
        return ResponseEntity.ok(tipoUsuarioService.buscarTipoUsuarios());
    }
}
