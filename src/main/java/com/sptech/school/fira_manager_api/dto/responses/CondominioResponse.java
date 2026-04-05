package com.sptech.school.fira_manager_api.dto.responses;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CondominioDTO", description = "DTO usado para criar ou atualizar um Condominio.")
public class CondominioResponse {

    @Schema(description = "Nome do Condominio", example = "Condomínio LIV", required = true)
    private String nome;

    @Schema(description = "Cidade do Condominio", example = "São Paulo", required = true)
    private String cidade;

    @Schema(description = "Bairro do Condominio", example = "São Miguel Paulista", required = true)
    private String bairro;

    @Schema(description = "Rua do Condominio", example = "Rua Santo Antônio", required = true)
    private String rua;

    @Schema(description = "Número do Condominio", example = "571", required = true)
    private String numero;

    public CondominioResponse(String nome, String cidade, String bairro, String rua, String numero) {
        this.nome = nome;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
    }

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

