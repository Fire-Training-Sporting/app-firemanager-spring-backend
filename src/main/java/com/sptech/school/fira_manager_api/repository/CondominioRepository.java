package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CondominioRepository extends JpaRepository<Condominio, Long>, JpaSpecificationExecutor<Condominio> {
}
