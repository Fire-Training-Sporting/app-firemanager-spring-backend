package com.sptech.school.fira_manager_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sptech.school.fira_manager_api.mapper.usuario.UsuarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioTokenResponse;
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
import com.sptech.school.fira_manager_api.repository.AgendamentoRepository;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import com.sptech.school.fira_manager_api.repository.SaldoRepository;
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
    private final AgendamentoRepository agendamentoRepository;
    private final SaldoRepository saldoRepository;
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private static final int MAX_TENTATIVAS = 5;
    private static final int MINUTOS_BLOQUEADOS = 15;
    private static final List<String> STATUS_AGENDAMENTO_ATIVO = List.of("pendente", "confirmado");
    private final Map<String, ControleLoginService> controleLogins = new ConcurrentHashMap<>();

    public UsuarioService(UsuarioRepository usuarioRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          CondominioRepository condominioRepository,
                          PasswordEncoder passwordEncoder,
                          GerenciadorTokenJwt gerenciadorTokenJwt,
                          @Lazy AuthenticationManager authenticationManager,
                          AgendamentoRepository agendamentoRepository,
                          SaldoRepository saldoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.condominioRepository = condominioRepository;
        this.passwordEncoder = passwordEncoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.agendamentoRepository = agendamentoRepository;
        this.saldoRepository = saldoRepository;
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

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            boolean arquivado = usuarioRepository.findByEmail(dto.getEmail())
                    .map(Usuario::isArquivado)
                    .orElse(false);
            throw new ResponseStatusException(HttpStatus.CONFLICT, arquivado
                    ? "Email pertence a um usuário arquivado. Desarquive-o em vez de cadastrar novamente"
                    : "Email já cadastrado");
        }

        if (usuarioRepository.existsByTelefone(dto.getTelefone())) {
            boolean arquivado = usuarioRepository.findByTelefone(dto.getTelefone())
                    .map(Usuario::isArquivado)
                    .orElse(false);
            throw new ResponseStatusException(HttpStatus.CONFLICT, arquivado
                    ? "Telefone pertence a um usuário arquivado. Desarquive-o em vez de cadastrar novamente"
                    : "Telefone já cadastrado");
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

            Usuario usuarioAluno = UsuarioMapper.toEntity(dto, tipoUsuario, senhaCriptografada, condominio);
            return UsuarioMapper.toResponse(usuarioRepository.save(usuarioAluno));
        }

        Usuario usuarioNovo = UsuarioMapper.toEntity(dto, tipoUsuario, senhaCriptografada, null);

        Usuario usuarioSalvo = usuarioRepository.save(usuarioNovo);

        log.info("Usuário criado - id={}, tipoUsuario={}",
                usuarioSalvo.getId(), tipoUsuario.getCargo());

        return UsuarioMapper.toResponse(usuarioSalvo);
    }

    public UsuarioTokenResponse logarUsuario(LoginRequest dto) {

        ControleLoginService controle = controleLogins.computeIfAbsent(dto.getEmail(), email -> new ControleLoginService());

        if (controle.estaBloqueado()) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Conta bloqueada. Tente novamente mais tarde.");
        }

        try {
            Authentication autenticacao = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
            );

            SecurityContextHolder.getContext().setAuthentication(autenticacao);

            controle.resetar();

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

            log.info("Login realizado - email={}", dto.getEmail());

            return UsuarioMapper.toTokenResponse(usuario, token);

        } catch (BadCredentialsException e) {

            controle.incrementar();

            if (controle.getTentativas() >= MAX_TENTATIVAS) {
                controle.bloquear(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEADOS));
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");
        } catch (DisabledException e) {
            controle.resetar();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Usuário arquivado. Entre em contato com a administração");
        }
    }

    public PaginaResponse<UsuarioResponse> buscarUsuarios(
            Pageable pageable,
            String nome,
            List<Long> tipoUsuarioIds,
            List<String> tipoUsuarioCargos,
            String campo,
            String busca,
            String status) {
        Pageable pageableEstavel = comOrdenacaoEstavel(pageable);
        String statusBusca = status == null || status.isBlank() ? "ATIVO" : status.trim().toUpperCase(Locale.ROOT);
        if (!List.of("ATIVO", "ARQUIVADO", "TODOS").contains(statusBusca)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido. Use ATIVO, ARQUIVADO ou TODOS");
        }
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

            switch (statusBusca) {
                case "ATIVO" -> predicates.add(criteriaBuilder.isNull(root.get("arquivadoEm")));
                case "ARQUIVADO" -> predicates.add(criteriaBuilder.isNotNull(root.get("arquivadoEm")));
                default -> { }
            }

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

            Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
            boolean professor = autenticacao != null && autenticacao.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_PROFESSOR"));
            if (professor) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.join("tipoUsuario").get("cargo")), "aluno"));
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
        return PaginaResponse.from(usuarios.map(UsuarioMapper::toResponse));
    }

    public PaginaResponse<UsuarioResponse> buscarUsuarios(
            Pageable pageable,
            String nome,
            List<Long> tipoUsuarioIds,
            List<String> tipoUsuarioCargos,
            String campo,
            String busca) {
        return buscarUsuarios(pageable, nome, tipoUsuarioIds, tipoUsuarioCargos, campo, busca, null);
    }

    public PaginaResponse<UsuarioResponse> buscarUsuarios(Pageable pageable, String nome, List<Long> tipoUsuarioIds) {
        return buscarUsuarios(pageable, nome, tipoUsuarioIds, null, null, null, null);
    }

    public UsuarioResponse buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));
        return UsuarioMapper.toResponse(usuario);
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

        if (usuarioNovo.isArquivado()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Usuário arquivado. Desarquive-o antes de editar");
        }

        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        boolean alunoAtualizandoPerfil = autenticacao != null && autenticacao.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ALUNO"));
        if (alunoAtualizandoPerfil
                && (!dto.getTipoUsuario().equals(usuarioNovo.getTipoUsuario().getId())
                    || !java.util.Objects.equals(dto.getCondominio(),
                            usuarioNovo.getCondominio() == null ? null : usuarioNovo.getCondominio().getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Alunos não podem alterar o próprio tipo de usuário ou condomínio");
        }

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

        return UsuarioMapper.toResponse(usuarioRepository.save(usuarioNovo));
    }

    public void deletarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));

        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (!possuiCargo(autenticacao, "ROLE_ROOT")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Somente root pode excluir usuários definitivamente");
        }

        if (ehUsuarioAutenticado(autenticacao, usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Não é possível excluir o próprio usuário");
        }

        if (agendamentoRepository.existeAgendamentoDoUsuario(id) || saldoRepository.existsByAlunoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Usuário possui histórico de agendamentos ou saldos. Arquive-o em vez de excluir");
        }

        usuarioRepository.delete(usuario);

        log.info("Usuário deletado - id={}",
                id);
    }

    public void arquivarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));

        validarPermissaoArquivamento(usuario);

        if (usuario.isArquivado()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já está arquivado");
        }

        if (usuario.getTipoUsuario().getCargo().equalsIgnoreCase("root")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuários root não podem ser arquivados");
        }

        long agendamentosFuturos = agendamentoRepository.contarAgendamentosFuturosDoUsuario(
                id, LocalDate.now(), STATUS_AGENDAMENTO_ATIVO);
        if (agendamentosFuturos > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Usuário possui " + agendamentosFuturos
                            + " agendamento(s) futuro(s) pendente(s) ou confirmado(s). Cancele-os antes de arquivar");
        }

        usuario.setArquivadoEm(LocalDateTime.now());
        usuarioRepository.save(usuario);

        log.info("Usuário arquivado - id={}", id);
    }

    public void desarquivarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O usuário não existe"));

        validarPermissaoArquivamento(usuario);

        if (!usuario.isArquivado()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário não está arquivado");
        }

        usuario.setArquivadoEm(null);
        usuarioRepository.save(usuario);

        log.info("Usuário desarquivado - id={}", id);
    }

    private void validarPermissaoArquivamento(Usuario usuario) {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (!possuiCargo(autenticacao, "ROLE_ROOT") && !possuiCargo(autenticacao, "ROLE_ADMINISTRACAO")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Somente administradores podem arquivar ou desarquivar usuários");
        }

        if (administracaoNaoPodeRemoverCargo(autenticacao, usuario.getTipoUsuario())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Somente root pode arquivar ou desarquivar usuários root ou de administração");
        }

        if (ehUsuarioAutenticado(autenticacao, usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Não é possível arquivar ou desarquivar o próprio usuário");
        }
    }

    private boolean possuiCargo(Authentication autenticacao, String cargo) {
        return autenticacao != null && autenticacao.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(cargo));
    }

    private boolean ehUsuarioAutenticado(Authentication autenticacao, Usuario usuario) {
        return autenticacao != null && usuario.getEmail() != null
                && usuario.getEmail().equalsIgnoreCase(autenticacao.getName());
    }

    private boolean administracaoNaoPodeRemoverCargo(Authentication autenticacao, TipoUsuario tipoUsuario) {
        boolean administracao = autenticacao != null && autenticacao.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMINISTRACAO"));
        String cargo = tipoUsuario.getCargo();
        return administracao && (cargo.equalsIgnoreCase("root") || cargo.equalsIgnoreCase("administracao"));
    }
}
