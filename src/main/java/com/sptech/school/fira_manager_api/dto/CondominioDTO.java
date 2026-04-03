package com.sptech.school.fira_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CondominioDTO", description = "DTO usado para criar ou atualizar um Condominio.")
public class CondominioDTO {

    @Schema(description = "Cidade do Condominio", example = "São Paulo", required = true)
    @NotBlank(message = "Cidade é obrigatório")
    private String cidade;

    @Schema(description = "Bairro do Condominio", example = "São Miguel Paulista", required = true)
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;

    @Schema(description = "Rua do Condominio", example = "Rua Santo Antônio", required = true)
    @NotBlank(message = "Rua é obrigatório")
    private String rua;

    @Schema(description = "Número do Condominio", example = "571", required = true)
    @NotBlank(message = "Número é obrigatório")
    private String numero;

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