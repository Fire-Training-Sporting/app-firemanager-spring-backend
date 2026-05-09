package com.sptech.school.fira_manager_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sptech.school.fira_manager_api.dto.LoginDTO;
import com.sptech.school.fira_manager_api.dto.UsuarioDTO;
import com.sptech.school.fira_manager_api.dto.UsuarioTokenDto;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários — Somente administradores podem criar, atualizar ou deletar")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Cria um novo usuário", description = "Cria um usuário no sistema. Somente administradores podem acessar este endpoint")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email já cadastrado",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criarUsuario(dto));
    }

    @Operation(summary = "Login de usuário", description = "Autentica um usuário e retorna seus dados básicos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login bem-sucedido",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Senha inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> logarUsuario(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(usuarioService.logarUsuario(dto));
    }

    @Operation(summary = "Lista usuários", description = "Retorna todos os usuários cadastrados ou filtra por nome")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            )
    })
    @GetMapping()
    public ResponseEntity<List<Usuario>> buscarUsuarios(@RequestParam(required = false) String nome) {
        if (nome == null) {
            return ResponseEntity.ok(usuarioService.buscarUsuarios());
        } else {
            return ResponseEntity.ok(usuarioService.buscarUsuarioPorNome(nome));
        }
    }

    @Operation(summary = "Busca usuário por ID", description = "Retorna os dados de um usuário específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @Operation(summary = "Atualiza usuário por ID", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuarioPorId(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarUsuarioPorId(id, dto));
    }

    @Operation(summary = "Deleta usuário por ID", description = "Deleta um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário deletado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuarioPorId(@PathVariable Long id) {
        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
