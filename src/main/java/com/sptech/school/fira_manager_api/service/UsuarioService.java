package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.Usuario;
import com.sptech.school.fira_manager_api.model.ETipoUsuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final List<Usuario> usuarios = new ArrayList<>();
    private Long id = -1L;

    public Usuario criarUsuario(
            ETipoUsuario tipoUsuario,
            String nome,
            String email,
            String numero,
            String senha
    ) {
        id++;
        Usuario novoUsuario = new Usuario(id, tipoUsuario, nome, email, numero, senha);
        usuarios.add(novoUsuario);
        return novoUsuario;
    }

    public List<Usuario> obterUsuarios() { return usuarios; }

    public Usuario atualizarUsuario(Long id, Usuario usuario) {

        for (int i = 0; i < usuarios.size(); i++) {

            if (usuarios.get(i).getId() == id) {
                usuarios.get(i).setTipoUsuario(usuario.getTipoUsuario());
                usuarios.get(i).setNome(usuario.getNome());
                usuarios.get(i).setEmail(usuario.getEmail());
                usuarios.get(i).setNumero(usuario.getNumero());
                usuarios.get(i).setSenha(usuario.getSenha());
                return usuarios.get(i);
            }
        }

        return null;
    }

    public Boolean deletarUsuario(Long id) {

        for (int i = 0; i < usuarios.size(); i++) {

            if (usuarios.get(i).getId() == id) {
                usuarios.remove(usuarios.get(i));
                return true;
            }
        }

        return false;
    }
}
