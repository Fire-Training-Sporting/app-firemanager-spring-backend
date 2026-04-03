package com.sptech.school.fira_manager_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_condominios")
public class Condominio {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cidade é um campo obrigatório")
    private String cidade;

    @NotBlank(message = "Bairro é um campo obrigatório")
    private String bairro;

    @NotBlank(message = "Rua é um campo obrigatório")
    private String rua;

    @NotBlank(message = "Número é um campo obrigatório")
    private String numero;

    public Condominio() {
    }

    public Condominio(Long id, String cidade, String bairro, String rua, String numero) {
        this.id = id;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
    }

    public Condominio(String cidade, String bairro, String rua, String numero) {
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