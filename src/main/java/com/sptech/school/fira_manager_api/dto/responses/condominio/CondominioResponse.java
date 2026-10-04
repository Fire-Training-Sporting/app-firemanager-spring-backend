package com.sptech.school.fira_manager_api.dto.responses.condominio;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CondominioResponse", description = "Dados de resposta de um Condomínio.")
public class CondominioResponse {

    @Schema(description = "ID do Condomínio", example = "1")
    private Long id;

    @Schema(description = "Nome do Condomínio", example = "Condomínio LIV")
    private String nome;

    @Schema(description = "Cidade do Condomínio", example = "São Paulo")
    private String cidade;

    @Schema(description = "Bairro do Condomínio", example = "São Miguel Paulista")
    private String bairro;

    @Schema(description = "Rua do Condomínio", example = "Rua Santo Antônio")
    private String rua;

    @Schema(description = "Número do Condomínio", example = "571")
    private String numero;

    @Schema(description = "CEP do Condomínio", example = "08000-000")
    private String cep;

    public CondominioResponse(Long id, String nome, String cidade, String bairro, String rua, String numero) {
        this(id, nome, cidade, bairro, rua, numero, null);
    }

    public CondominioResponse(Long id, String nome, String cidade, String bairro, String rua, String numero, String cep) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
    }

    public CondominioResponse(String nome, String cidade, String bairro, String rua, String numero) {
        this.nome = nome;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
