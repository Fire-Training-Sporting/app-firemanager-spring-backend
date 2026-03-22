package com.sptech.school.fira_manager_api.controller;

import com.sptech.school.fira_manager_api.dto.Usuario;
import com.sptech.school.fira_manager_api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping()
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {

        if (usuario.getTipoUsuario() == null ||
        usuario.getNome() == null ||
        usuario.getEmail() == null ||
        usuario.getNumero() == null ||
        usuario.getSenha() == null) return ResponseEntity.status(400).build();

        Usuario novoUsuario = usuarioService.criarUsuario(
                usuario.getTipoUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getNumero(),
                usuario.getSenha());

        return ResponseEntity.status(201).body(novoUsuario);
    }

    @GetMapping()
    public ResponseEntity<List<Usuario>> buscarUsuarios() {
        List<Usuario> usuarios = usuarioService.obterUsuarios();

        if (usuarios.size() == 0) return ResponseEntity.status(204).build();

        return ResponseEntity.status(200).body(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {

        if (usuario.getTipoUsuario() == null ||
        usuario.getNome() == null ||
        usuario.getEmail() == null ||
        usuario.getNumero() == null ||
        usuario.getSenha() == null) return ResponseEntity.status(400).build();

        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);

        if (usuarioAtualizado == null) return ResponseEntity.status(404).build();

        return ResponseEntity.status(200).body(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> deletarUsuario(@PathVariable Long id) {

        Boolean usuarioDeletado = usuarioService.deletarUsuario(id);

        if (!usuarioDeletado) return ResponseEntity.status(404).build();

        return ResponseEntity.status(200).build();
    }

}
