package com.sptech.school.fira_manager_api.repository;

import com.sptech.school.fira_manager_api.model.TokenRecuperacaoSenha;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenRecuperacaoSenhaRepository extends JpaRepository<TokenRecuperacaoSenha, Long> {

    Optional<TokenRecuperacaoSenha> findFirstByUsuario_IdOrderByCriadoEmDesc(Long usuarioId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select token from TokenRecuperacaoSenha token where token.tokenHash = :tokenHash")
    Optional<TokenRecuperacaoSenha> findByTokenHashForUpdate(@Param("tokenHash") String tokenHash);

    @Modifying
    @Transactional
    @Query("update TokenRecuperacaoSenha token set token.utilizado = true "
            + "where token.usuario.id = :usuarioId and token.utilizado = false")
    void invalidarTokensAtivosDoUsuario(@Param("usuarioId") Long usuarioId);
}
