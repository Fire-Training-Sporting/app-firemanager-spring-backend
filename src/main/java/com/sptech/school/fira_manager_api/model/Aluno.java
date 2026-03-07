package com.sptech.school.fira_manager_api.model;

public class Aluno {

    private int id;
    private String nome;
    private Saldo saldo;

    public Aluno() {
    }

    public Aluno(int id, String nome, Saldo saldo) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }
}
