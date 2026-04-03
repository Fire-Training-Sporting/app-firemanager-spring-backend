package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    Usuario findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByTelefone(String telefone);
}
