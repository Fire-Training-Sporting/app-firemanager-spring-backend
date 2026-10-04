package com.sptech.school.fira_manager_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sptech.school.fira_manager_api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Usuario> findByTipoUsuario_IdIn(List<Long> tipoUsuarioIds, Pageable pageable);
    Page<Usuario> findByTipoUsuario_IdInAndNomeContainingIgnoreCase(List<Long> tipoUsuarioIds, String nome, Pageable pageable);
    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByTelefone(String telefone);
    Boolean existsByNome(String nome);
}
