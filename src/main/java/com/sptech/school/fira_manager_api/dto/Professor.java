package com.sptech.school.fira_manager_api.dto;

public class Professor {

    private int id;
    private String nome;
    private FuncaoProfessor funcao;

    public Professor() {
    }

    public Professor(int id, String nome, FuncaoProfessor funcao) {
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

    public FuncaoProfessor getFuncao() {
        return funcao;
    }

    public void setFuncao(FuncaoProfessor funcao) {
        this.funcao = funcao;
    }
}
