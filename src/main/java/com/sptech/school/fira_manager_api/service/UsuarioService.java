package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.config.SegurancaConfig;
import com.sptech.school.fira_manager_api.dto.LoginDTO;
import com.sptech.school.fira_manager_api.dto.UsuarioDTO;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.model.ETipoUsuario;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CondominioRepository condominioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, CondominioRepository condominioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.condominioRepository = condominioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario criarUsuario(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Integer fkTipoUsuario = null;

        switch (dto.getTipoUsuario()) {
            case ROOT -> fkTipoUsuario = 1;
            case ADMINISTRATIVO -> fkTipoUsuario = 2;
            case PROFESSOR -> fkTipoUsuario = 3;
            case ALUNO -> fkTipoUsuario = 4;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de usuário inválido");
        }

        if (fkTipoUsuario.equals(4)) {
            if (dto.getCondominio() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Condomínio é obrigatório para alunos");
            }

            Condominio condominio = condominioRepository.findById(dto.getCondominio())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condomínio não encontrado"));

            Usuario usuarioAluno = new Usuario();
            usuarioAluno.setFkTipoUsuario(fkTipoUsuario);
            usuarioAluno.setNome(dto.getNome());
            usuarioAluno.setEmail(dto.getEmail());
            usuarioAluno.setTelefone(dto.getTelefone());
            usuarioAluno.setSenha(senhaCriptografada);
            usuarioAluno.setCondominio(condominio);

            return usuarioRepository.save(usuarioAluno);
        }

        Usuario usuarioNovo = new Usuario(fkTipoUsuario, dto.getNome(), dto.getEmail(), dto.getTelefone(), senhaCriptografada);
        return usuarioRepository.save(usuarioNovo);
    }

    public Usuario logarUsuario(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail());

        if (usuario == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida");

        return usuario;
    }

    public List<Usuario> buscarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios;
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));
    }

    public List<Usuario> buscarUsuarioPorNome(String nome) {
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);

        return usuarios;
    }

    public Usuario atualizarUsuarioPorId(Long id, UsuarioDTO dto) {
        Usuario usuarioNovo = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));

        Integer fkTipoUsuario = null;

        switch (dto.getTipoUsuario()) {
            case ROOT -> fkTipoUsuario = 1;
            case ADMINISTRATIVO -> fkTipoUsuario = 2;
            case PROFESSOR -> fkTipoUsuario = 3;
            case ALUNO -> fkTipoUsuario = 4;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de usuário inválido");
        }

        Integer fkCondominio = null;

        if (fkTipoUsuario.equals(4)) {
            if (dto.getCondominio() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Condomínio é obrigatório para alunos");
            }

            Condominio condominio = condominioRepository.findById(dto.getCondominio())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condomínio não encontrado"));

            usuarioNovo.setCondominio(condominio);
        } else {
            usuarioNovo.setCondominio(null);
        }

        usuarioNovo.setFkTipoUsuario(fkTipoUsuario);
        usuarioNovo.setNome(dto.getNome());
        usuarioNovo.setEmail(dto.getEmail());
        usuarioNovo.setTelefone(dto.getTelefone());
        usuarioNovo.setSenha(passwordEncoder.encode(dto.getSenha()));

        return usuarioRepository.save(usuarioNovo);
    }

    public void deletarUsuarioPorId(Long id) {
        if (!usuarioRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe");

        usuarioRepository.deleteById(id);
    }
}
