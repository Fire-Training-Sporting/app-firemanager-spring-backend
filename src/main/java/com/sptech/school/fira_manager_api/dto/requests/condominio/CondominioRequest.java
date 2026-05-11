package com.sptech.school.fira_manager_api.dto.requests.condominio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CondominioRequest", description = "Payload para criar ou atualizar um Condomínio.")
public class CondominioRequest {

    @Schema(description = "Nome do Condomínio", example = "Condomínio LIV", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Schema(description = "Cidade do Condomínio", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @Schema(description = "Bairro do Condomínio", example = "São Miguel Paulista", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    private String bairro;

    @Schema(description = "Rua do Condomínio", example = "Rua Santo Antônio", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Rua é obrigatória")
    @Size(max = 150, message = "Rua deve ter no máximo 150 caracteres")
    private String rua;

    @Schema(description = "Número do Condomínio", example = "571", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
    private String numero;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
