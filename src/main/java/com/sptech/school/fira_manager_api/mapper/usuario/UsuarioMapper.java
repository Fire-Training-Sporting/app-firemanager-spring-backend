package com.sptech.school.fira_manager_api.mapper.usuario;

import com.sptech.school.fira_manager_api.dto.requests.usuario.UsuarioRequest;
import com.sptech.school.fira_manager_api.dto.responses.condominio.CondominioResponse;
import com.sptech.school.fira_manager_api.dto.responses.tipoUsuario.TipoUsuarioResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioTokenResponse;
import com.sptech.school.fira_manager_api.mapper.condominio.CondominioMapper;
import com.sptech.school.fira_manager_api.mapper.tipoUsuario.TipoUsuarioMapper;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.model.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {}

    public static Usuario toEntity(UsuarioRequest dto, TipoUsuario tipoUsuario,
                                   String senhaCriptografada, Condominio condominio) {
        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setSenha(senhaCriptografada);
        usuario.setCondominio(condominio);
        return usuario;
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        TipoUsuarioResponse tipoResponse = usuario.getTipoUsuario() != null
                ? TipoUsuarioMapper.toResponse(usuario.getTipoUsuario())
                : null;

        CondominioResponse condominioResponse = usuario.getCondominio() != null
                ? CondominioMapper.toResponse(usuario.getCondominio())
                : null;

        return new UsuarioResponse(
                usuario.getId(),
                tipoResponse,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                condominioResponse,
                usuario.getCriadoEm()
        );
    }

    public static UsuarioTokenResponse toTokenResponse(Usuario usuario, String token) {
        String cargo = usuario.getTipoUsuario() != null
                ? usuario.getTipoUsuario().getCargo()
                : null;

        return new UsuarioTokenResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                cargo,
                token
        );
    }
}