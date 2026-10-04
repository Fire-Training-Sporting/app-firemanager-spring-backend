package com.sptech.school.fira_manager_api.service;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public PaginaResponse<UsuarioResponse> buscarUsuarios(
            Pageable pageable,
            String nome,
            List<Long> tipoUsuarioIds,
            List<String> tipoUsuarioCargos,
            String campo,
            String busca) {
        Pageable pageableEstavel = comOrdenacaoEstavel(pageable);
        String valorBusca = busca == null || busca.isBlank() ? nome : busca;
        String campoBusca = campo == null || campo.isBlank() ? "nome" : campo;
        String termo = valorBusca == null ? null : valorBusca.trim().toLowerCase(Locale.ROOT);

        if (termo != null && !termo.isBlank()
                && !List.of("id", "nome", "email", "telefone", "endereco", "tipoUsuario.cargo")
                        .contains(campoBusca)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de busca inválido");
        }

        Specification<Usuario> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tipoUsuarioIds != null && !tipoUsuarioIds.isEmpty()) {
                predicates.add(root.join("tipoUsuario").get("id").in(tipoUsuarioIds));
            }

            if (tipoUsuarioCargos != null && !tipoUsuarioCargos.isEmpty()) {
                List<String> cargosNormalizados = tipoUsuarioCargos.stream()
                        .filter(cargo -> cargo != null && !cargo.isBlank())
                        .map(cargo -> cargo.trim().toLowerCase(Locale.ROOT))
                        .toList();
                if (!cargosNormalizados.isEmpty()) {
                    predicates.add(criteriaBuilder.lower(root.join("tipoUsuario").get("cargo"))
                            .in(cargosNormalizados));
                }
            }

            if (termo != null && !termo.isBlank()) {
                String padrao = "%" + termo + "%";
                switch (campoBusca) {
                    case "id" -> predicates.add(criteriaBuilder.like(
                            root.get("id").as(String.class), padrao));
                    case "nome", "email", "telefone" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(campoBusca)), padrao));
                    case "endereco" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.join("condominio", JoinType.LEFT).get("nome")), padrao));
                    case "tipoUsuario.cargo" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.join("tipoUsuario").get("cargo")), padrao));
                    default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de busca inválido");
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Usuario> usuarios = usuarioRepository.findAll(specification, pageableEstavel);
        return PaginaResponse.from(usuarios.map(this::toResponse));
    }

    public PaginaResponse<UsuarioResponse> buscarUsuarios(Pageable pageable, String nome, List<Long> tipoUsuarioIds) {
        return buscarUsuarios(pageable, nome, tipoUsuarioIds, null, null, null);
    }

    public UsuarioResponse buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));
        return toResponse(usuario);
    }

    private Pageable comOrdenacaoEstavel(Pageable pageable) {
        List<Sort.Order> outrasOrdenacoes = pageable.getSort().stream()
                .filter(order -> !order.getProperty().equals("id"))
                .toList();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        if (!outrasOrdenacoes.isEmpty()) {
            sort = sort.and(Sort.by(outrasOrdenacoes));
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
