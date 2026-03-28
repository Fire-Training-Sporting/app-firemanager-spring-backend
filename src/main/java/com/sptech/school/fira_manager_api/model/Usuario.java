package com.sptech.school.fira_manager_api.model;

//import com.sptech.school.fira_manager_api.dto.Condominio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fk_tipo_usuario", nullable = false)
    private Integer fkTipoUsuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String telefone;

    @JsonIgnore
    @Column(nullable = false)
    private String senha;

//    @NotBlank(message = "Condomínio é obrigatório")
//    private Condominio condominio;


    public Usuario() {
    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario(Integer fkTipoUsuario, String nome, String email, String telefone, String senha) {
        this.fkTipoUsuario = fkTipoUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Usuario(Long id, Integer fkTipoUsuario, String nome, String email, String telefone, String senha) {
        this.id = id;
        this.fkTipoUsuario = fkTipoUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFkTipoUsuario() {
        return fkTipoUsuario;
    }

    public void setFkTipoUsuario(Integer fkTipoUsuario) {
        this.fkTipoUsuario = fkTipoUsuario;
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

//    public Condominio getCondominio() {
//        return condominio;
//    }
//
//    public void setCondominio(Condominio condominio) {
//        this.condominio = condominio;
//    }
}
