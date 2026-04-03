package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
}