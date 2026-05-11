package com.sptech.school.fira_manager_api.dto.requests.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "UsuarioRequest", description = "Payload para cadastro ou atualização de usuário.")
public class UsuarioRequest {

    @Schema(description = "ID do tipo de usuário", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Tipo de usuário é obrigatório")
    private Long tipoUsuario;

    @Schema(description = "Nome completo do usuário", example = "Marcos Vinicius Albano", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "marcos.vinicius@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "E-Mail é obrigatório")
    @Email(message = "E-Mail inválido")
    @Size(max = 150, message = "E-mail deve ter no máximo 150 caracteres")
    private String email;

    @Schema(description = "Telefone do usuário (11 dígitos, apenas números)", example = "11948573097", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Número do celular é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "Telefone deve conter exatamente 11 dígitos")
    private String telefone;

    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 100, message = "Senha deve conter entre 6 e 100 caracteres")
    private String senha;

    @Schema(description = "ID do Condomínio em que o aluno recebe aulas (obrigatório para alunos)", example = "1")
    private Long condominio;

    public Long getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Long tipoUsuario) {
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

    public Long getCondominio() {
        return condominio;
    }

    public void setCondominio(Long condominio) {
        this.condominio = condominio;
    }
}
