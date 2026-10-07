package com.sptech.school.fira_manager_api.config;

import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.TipoUsuarioRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cria o usuário root inicial SOMENTE quando tb_usuarios está completamente vazia.
 * Não usa UsuarioService.criarUsuario porque ele exige um admin logado no SecurityContext.
 */
@Component
public class RootInicializador implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RootInicializador.class);
    private static final Long ID_TIPO_ROOT = 1L;

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private final String nome;
    private final String email;
    private final String telefone;
    private final String senha;

    public RootInicializador(UsuarioRepository usuarioRepository,
                             TipoUsuarioRepository tipoUsuarioRepository,
                             PasswordEncoder passwordEncoder,
                             @Value("${app.bootstrap.root.nome:root}") String nome,
                             @Value("${app.bootstrap.root.email:root@gmail.com}") String email,
                             @Value("${app.bootstrap.root.telefone:11987654321}") String telefone,
                             @Value("${app.bootstrap.root.senha:}") String senha) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (usuarioRepository.count() > 0) {
            return;
        }

        if (senha == null || senha.isBlank()) {
            log.warn("Nenhum usuário cadastrado e app.bootstrap.root.senha não definida: root inicial NÃO foi criado.");
            return;
        }

        TipoUsuario tipoRoot = tipoUsuarioRepository.findById(ID_TIPO_ROOT)
                .filter(tipo -> "root".equalsIgnoreCase(tipo.getCargo()))
                .orElse(null);

        if (tipoRoot == null) {
            log.error("Tipo de usuário 'root' (id={}) não encontrado: root inicial NÃO foi criado. "
                    + "Verifique se os scripts de init do banco rodaram.", ID_TIPO_ROOT);
            return;
        }

        Usuario root = new Usuario();
        root.setTipoUsuario(tipoRoot);
        root.setNome(nome);
        root.setEmail(email);
        root.setTelefone(telefone);
        root.setSenha(passwordEncoder.encode(senha));
        root.setCondominio(null);

        Usuario salvo = usuarioRepository.save(root);

        log.warn("Usuário root inicial criado - id={}, email={}. Troque a senha após o primeiro login.",
                salvo.getId(), salvo.getEmail());
    }
}