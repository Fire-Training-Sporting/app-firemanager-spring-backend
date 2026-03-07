package com.sptech.school.fira_manager_api.model;

public class Professor {

    private int id;
    private String nome;
    private EFuncaoProfessor funcao;

    public Professor() {
    }

    public Professor(int id, String nome, EFuncaoProfessor funcao) {
        this.id = id;
        this.nome = nome;
        this.funcao = funcao;
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

    public EFuncaoProfessor getFuncao() {
        return funcao;
    }

    public void setFuncao(EFuncaoProfessor funcao) {
        this.funcao = funcao;
    }
}
