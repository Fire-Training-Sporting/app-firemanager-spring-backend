package com.sptech.school.fira_manager_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "nome",
        "email",
        "telefone",
        "tipoUsuario",
        "condominio",
        "criadoEm"
})
@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_tipo_usuario", nullable = false)
    @NotNull(message = "Tipo de usuário é obrigatório")
    private TipoUsuario tipoUsuario;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String telefone;

    @JsonIgnore
    @Column(nullable = false)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "fk_condominio")
    private Condominio condominio;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por", updatable = false)
    private Usuario criadoPor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "arquivado_em")
    private LocalDateTime arquivadoEm;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arquivado_por")
    private Usuario arquivadoPor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "desarquivado_em")
    private LocalDateTime desarquivadoEm;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desarquivado_por")
    private Usuario desarquivadoPor;

    public Usuario() {
    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario(TipoUsuario tipoUsuario, String nome, String email, String telefone, String senha) {
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Usuario(TipoUsuario tipoUsuario, String nome, String email, String telefone, String senha, Condominio condominio) {
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.condominio = condominio;
    }

    public Usuario(Long id, TipoUsuario tipoUsuario, String nome, String email, String telefone, String senha) {
        this.id = id;
        this.tipoUsuario = tipoUsuario;
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
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

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public Usuario getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Usuario criadoPor) {
        this.criadoPor = criadoPor;
    }

    public LocalDateTime getArquivadoEm() {
        return arquivadoEm;
    }

    public void setArquivadoEm(LocalDateTime arquivadoEm) {
        this.arquivadoEm = arquivadoEm;
    }

    public Usuario getArquivadoPor() {
        return arquivadoPor;
    }

    public void setArquivadoPor(Usuario arquivadoPor) {
        this.arquivadoPor = arquivadoPor;
    }

    public LocalDateTime getDesarquivadoEm() {
        return desarquivadoEm;
    }

    public void setDesarquivadoEm(LocalDateTime desarquivadoEm) {
        this.desarquivadoEm = desarquivadoEm;
    }

    public Usuario getDesarquivadoPor() {
        return desarquivadoPor;
    }

    public void setDesarquivadoPor(Usuario desarquivadoPor) {
        this.desarquivadoPor = desarquivadoPor;
    }

    @JsonIgnore
    public boolean isArquivado() {
        return arquivadoEm != null;
    }
}
