package com.sptech.school.fira_manager_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sptech.school.fira_manager_api.config.GerenciadorTokenJwt;
import com.sptech.school.fira_manager_api.dto.UsuarioDetalhesDto;
import com.sptech.school.fira_manager_api.dto.requests.usuario.LoginRequest;
import com.sptech.school.fira_manager_api.dto.requests.usuario.UsuarioRequest;
import com.sptech.school.fira_manager_api.dto.responses.condominio.CondominioResponse;
import com.sptech.school.fira_manager_api.dto.responses.tipoUsuario.TipoUsuarioResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioTokenResponse;
import com.sptech.school.fira_manager_api.dto.responses.PaginaResponse;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import com.sptech.school.fira_manager_api.repository.TipoUsuarioRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final CondominioRepository condominioRepository;
    private final PasswordEncoder passwordEncoder;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          CondominioRepository condominioRepository,
                          PasswordEncoder passwordEncoder,
                          GerenciadorTokenJwt gerenciadorTokenJwt,
                          @Lazy AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.condominioRepository = condominioRepository;
        this.passwordEncoder = passwordEncoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        TipoUsuarioResponse tipoResponse = usuario.getTipoUsuario() != null
                ? new TipoUsuarioResponse(usuario.getTipoUsuario().getId(), usuario.getTipoUsuario().getCargo())
                : null;

        CondominioResponse condominioResponse = null;
        if (usuario.getCondominio() != null) {
            Condominio c = usuario.getCondominio();
            condominioResponse = new CondominioResponse(c.getId(), c.getNome(), c.getCidade(), c.getBairro(), c.getRua(), c.getNumero());
        }

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

    public UsuarioResponse criarUsuario(UsuarioRequest dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.isAuthenticated() &&
                auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRACAO")
                                || a.getAuthority().equals("ROLE_ROOT"));

        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente administradores podem criar usuários");
        }

        if (usuarioRepository.existsByNome(dto.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Alguém com este nome já cadastrado");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        if (usuarioRepository.existsByTelefone(dto.getTelefone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Telefone já cadastrado");
        }

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(dto.getTipoUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de usuário não encontrado"));

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        if (tipoUsuario.getId().equals(4L)) {
            if (dto.getCondominio() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Condomínio é obrigatório para alunos");
            }

            Condominio condominio = condominioRepository.findById(dto.getCondominio())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condomínio não encontrado"));

            Usuario usuarioAluno = new Usuario();
            usuarioAluno.setTipoUsuario(tipoUsuario);
            usuarioAluno.setNome(dto.getNome());
            usuarioAluno.setEmail(dto.getEmail());
            usuarioAluno.setTelefone(dto.getTelefone());
            usuarioAluno.setSenha(senhaCriptografada);
            usuarioAluno.setCondominio(condominio);

            return toResponse(usuarioRepository.save(usuarioAluno));
        }

        Usuario usuarioNovo = new Usuario(tipoUsuario, dto.getNome(), dto.getEmail(), dto.getTelefone(), senhaCriptografada);
        return toResponse(usuarioRepository.save(usuarioNovo));
    }

    public UsuarioTokenResponse logarUsuario(LoginRequest dto) {
        Authentication autenticacao = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );

        SecurityContextHolder.getContext().setAuthentication(autenticacao);

        Object principal = autenticacao.getPrincipal();
        UsuarioDetalhesDto usuarioDetalhes;

        if (principal instanceof UsuarioDetalhesDto) {
            usuarioDetalhes = (UsuarioDetalhesDto) principal;
        } else {
            String email = principal.toString();
            Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado após autenticação"));
            usuarioDetalhes = new UsuarioDetalhesDto(usuarioEntity);
        }

        String token = gerenciadorTokenJwt.generateToken(usuarioDetalhes);

        Usuario usuario = usuarioDetalhes.getUsuario();
        String cargo = usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getCargo() : null;
        return new UsuarioTokenResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), cargo, token);
    }

    public PaginaResponse<UsuarioResponse> buscarUsuarios(Pageable pageable, String nome, List<Long> tipoUsuarioIds) {
        Pageable pageableEstavel = comOrdenacaoEstavel(pageable);
        String nomeNormalizado = nome == null || nome.isBlank() ? null : nome.trim();

        Page<Usuario> usuarios;
        if (tipoUsuarioIds != null && !tipoUsuarioIds.isEmpty()) {
            usuarios = nomeNormalizado == null
                    ? usuarioRepository.findByTipoUsuario_IdIn(tipoUsuarioIds, pageableEstavel)
                    : usuarioRepository.findByTipoUsuario_IdInAndNomeContainingIgnoreCase(tipoUsuarioIds, nomeNormalizado, pageableEstavel);
        } else {
            usuarios = nomeNormalizado == null
                    ? usuarioRepository.findAll(pageableEstavel)
                    : usuarioRepository.findByNomeContainingIgnoreCase(nomeNormalizado, pageableEstavel);
        }

        return PaginaResponse.from(usuarios.map(this::toResponse));
    }

    public UsuarioResponse buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));
        return toResponse(usuario);
    }

    private Pageable comOrdenacaoEstavel(Pageable pageable) {
        Sort sort = pageable.getSort();
        if (sort.getOrderFor("id") == null) {
            sort = sort.and(Sort.by(Sort.Direction.ASC, "id"));
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    public UsuarioResponse atualizarUsuarioPorId(Long id, UsuarioRequest dto) {
        Usuario usuarioNovo = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(dto.getTipoUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de usuário não encontrado"));

        if (tipoUsuario.getId().equals(4L)) {
            if (dto.getCondominio() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Condomínio é obrigatório para alunos");
            }

            Condominio condominio = condominioRepository.findById(dto.getCondominio())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condomínio não encontrado"));
            usuarioNovo.setCondominio(condominio);
        } else {
            usuarioNovo.setCondominio(null);
        }

        usuarioNovo.setTipoUsuario(tipoUsuario);
        usuarioNovo.setNome(dto.getNome());
        usuarioNovo.setEmail(dto.getEmail());
        usuarioNovo.setTelefone(dto.getTelefone());
        usuarioNovo.setSenha(passwordEncoder.encode(dto.getSenha()));

        return toResponse(usuarioRepository.save(usuarioNovo));
    }

    public void deletarUsuarioPorId(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe");
        }
        usuarioRepository.deleteById(id);
    }
}
