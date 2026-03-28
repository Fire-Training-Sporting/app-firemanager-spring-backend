package com.sptech.school.fira_manager_api.dto;

import com.sptech.school.fira_manager_api.model.ETipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "UsuarioDTO", description = "DTO para cadastro ou atualização de usuário")
public class UsuarioDTO {

    @Schema(description = "Tipo do usuário", example = "ADMINISTRATIVO", required = true)
    @NotNull(message = "Tipo de usuário é obrigatório")
    private ETipoUsuario tipoUsuario;

    @Schema(description = "Nome completo do usuário", example = "Marcos Vinicius Albano", required = true)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "marcos.vinicius@email.com", required = true)
    @NotBlank(message = "E-Mail é obrigatório")
    @Email(message = "E-Mail inválido")
    private String email;

    @Schema(description = "Telefone do usuário (11 dígitos, apenas números)", example = "11475586548", required = true)
    @NotBlank(message = "Número do celular é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "Telefone deve conter exatamente 11 dígitos")
    private String telefone;

    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "senha123", required = true)
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve conter no mínimo 6 caracteres")
    private String senha;

//    private Condominio condominio;


    public ETipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(ETipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
