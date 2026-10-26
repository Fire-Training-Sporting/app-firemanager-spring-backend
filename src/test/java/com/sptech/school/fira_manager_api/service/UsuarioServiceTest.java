package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.config.GerenciadorTokenJwt;
import com.sptech.school.fira_manager_api.dto.UsuarioDetalhesDto;
import com.sptech.school.fira_manager_api.dto.requests.usuario.LoginRequest;
import com.sptech.school.fira_manager_api.dto.requests.usuario.UsuarioRequest;
import com.sptech.school.fira_manager_api.dto.responses.PaginaResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioTokenResponse;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.AgendamentoRepository;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import com.sptech.school.fira_manager_api.repository.SaldoRepository;
import com.sptech.school.fira_manager_api.repository.TipoUsuarioRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private TipoUsuarioRepository tipoUsuarioRepository;
    @Mock private CondominioRepository condominioRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private SaldoRepository saldoRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @AfterEach
    void limparAutenticacao() {
        SecurityContextHolder.clearContext();
    }

    private void mockAutenticacaoAdmin() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMINISTRACAO"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Nested
    class CriarUsuario {

        @Test
        @DisplayName("Cadastrar Funcionário Sucesso ")
        void cadastrarUsuarioFuncionario() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(3L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(false);
            when(usuarioRepository.existsByTelefone("11935234123")).thenReturn(false);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(3L);
            tipo.setCargo("professor");
            when(tipoUsuarioRepository.findById(3L)).thenReturn(Optional.of(tipo));

            when(passwordEncoder.encode("teste123*")).thenReturn("luanWasHere");

            Usuario usuarioSalvo = new Usuario(tipo, "Marcos Vinicius", "marcos@gmail.com", "11935234123", "luanWasHere");
            usuarioSalvo.setId(1L);
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

            UsuarioResponse response = usuarioService.criarUsuario(request);
            assertNotNull(response);
            assertEquals("Marcos Vinicius", response.getNome());
            assertEquals("marcos@gmail.com", response.getEmail());
            assertEquals("11935234123", response.getTelefone());
        }

        @Test
        @DisplayName("Administracao pode criar usuários root")
        void administracaoPodeCriarRoot() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(1L);
            request.setNome("Novo root");
            request.setEmail("root@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            when(usuarioRepository.existsByEmail("root@gmail.com")).thenReturn(false);
            when(usuarioRepository.existsByTelefone("11935234123")).thenReturn(false);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(1L);
            tipo.setCargo("root");
            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipo));

            when(passwordEncoder.encode("teste123*")).thenReturn("senhaCriptografada");
            Usuario usuarioSalvo = new Usuario(tipo, "Novo root", "root@gmail.com",
                    "11935234123", "senhaCriptografada");
            usuarioSalvo.setId(1L);
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

            UsuarioResponse response = usuarioService.criarUsuario(request);

            assertNotNull(response);
            assertEquals("Novo root", response.getNome());
            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Cadastrar Aluno Sucesso")
        void cadastrarUsuarioAluno() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(1L);

            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(false);
            when(usuarioRepository.existsByTelefone("11935234123")).thenReturn(false);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("Aluno");
            when(tipoUsuarioRepository.findById(4L)).thenReturn(Optional.of(tipo));

            when(passwordEncoder.encode("teste123*")).thenReturn("luanWasHere");

            Condominio condominio = new Condominio();
            condominio.setId(1L);
            condominio.setNome("Condomínio LIV");
            when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));

            Usuario usuarioSalvo = new Usuario(tipo, "Marcos Vinicius", "marcos@gmail.com", "11935234123", "senhaCriptografada");
            usuarioSalvo.setId(1L);
            usuarioSalvo.setCondominio(condominio);
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

            UsuarioResponse response = usuarioService.criarUsuario(request);
            assertNotNull(response);
            assertEquals("Marcos Vinicius", response.getNome());
            assertEquals("marcos@gmail.com", response.getEmail());
            assertEquals("11935234123", response.getTelefone());
            assertNotNull(response.getCondominio());
        }

        @Test
        @DisplayName("Cadastro Falho - Email Cadastrado")
        void cadastrarUsuarioEmailRepetido() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(1L);

            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(true);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.criarUsuario(request)
            );
            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Cadastro Falho - Telefone Cadastrado")
        void cadastrarUsuarioTelefoneRepetido() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(1L);

            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(false);
            when(usuarioRepository.existsByTelefone("11935234123")).thenReturn(true);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.criarUsuario(request)
            );
            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Cadastro Falho - Tipo Usuário Existe")
        void cadastrarUsuarioTipoUsuarioInvalido() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(99L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(false);
            when(usuarioRepository.existsByTelefone("11935234123")).thenReturn(false);
            when(tipoUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.criarUsuario(request)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Cadastro Falho - Aluno Condomínio Inexistente")
        void cadastrarUsuarioAlunoCondominioInvalido() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(100L);

            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(false);
            when(usuarioRepository.existsByTelefone("11935234123")).thenReturn(false);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("Aluno");
            when(tipoUsuarioRepository.findById(4L)).thenReturn(Optional.of(tipo));

            when(passwordEncoder.encode("teste123*")).thenReturn("luanWasHere");

            when(condominioRepository.findById(100L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.criarUsuario(request)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Cadastro Falho - Aluno sem Condomínio")
        void cadastrarUsuarioAlunoSemCondominio() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(false);
            when(usuarioRepository.existsByTelefone("11935234123")).thenReturn(false);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("Aluno");
            when(tipoUsuarioRepository.findById(4L)).thenReturn(Optional.of(tipo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.criarUsuario(request)
            );
            assertEquals(400, exception.getStatusCode().value());
        }
    }

    @Nested
    class Login {

        @Test
        @DisplayName("Login bloqueado após 5 tentativas falhas")
        void bloquearAposCincoTentativasFalhas() {
            LoginRequest request = new LoginRequest();
            request.setEmail("marcos@gmail.com");
            request.setSenha("senhaErrada");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Credenciais inválidas"));

            for (int i = 0; i < 5; i++) {
                ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                        () -> usuarioService.logarUsuario(request));
                assertEquals(401, exception.getStatusCode().value());
            }

            ResponseStatusException bloqueado = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.logarUsuario(request));
            assertEquals(423, bloqueado.getStatusCode().value());

            verify(authenticationManager, times(5)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        @DisplayName("Login com sucesso reseta contador de tentativas falhas")
        void loginSucessoResetaContador() {
            LoginRequest requestErrado = new LoginRequest();
            requestErrado.setEmail("marcos@gmail.com");
            requestErrado.setSenha("senhaErrada");

            LoginRequest requestCerto = new LoginRequest();
            requestCerto.setEmail("marcos@gmail.com");
            requestCerto.setSenha("teste123*");

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(1L);
            tipo.setCargo("root");

            Usuario usuario = new Usuario();
            usuario.setId(1L);
            usuario.setNome("Marcos Vinicius");
            usuario.setEmail("marcos@gmail.com");
            usuario.setTipoUsuario(tipo);

            UsuarioDetalhesDto detalhes = new UsuarioDetalhesDto(usuario);
            Authentication autenticacaoOk = mock(Authentication.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenAnswer(invocation -> {
                        UsernamePasswordAuthenticationToken token = invocation.getArgument(0);
                        if ("teste123*".equals(token.getCredentials())) {
                            return autenticacaoOk;
                        }
                        throw new BadCredentialsException("Credenciais inválidas");
                    });
            when(autenticacaoOk.getPrincipal()).thenReturn(detalhes);
            when(gerenciadorTokenJwt.generateToken(detalhes)).thenReturn("token-vitao");

            for (int i = 0; i < 4; i++) {
                assertThrows(ResponseStatusException.class, () -> usuarioService.logarUsuario(requestErrado));
            }

            assertNotNull(usuarioService.logarUsuario(requestCerto));

            // depois do sucesso, mais 4 erradas não deveriam bloquear (contador zerou)
            for (int i = 0; i < 4; i++) {
                assertThrows(ResponseStatusException.class, () -> usuarioService.logarUsuario(requestErrado));
            }

            UsuarioTokenResponse response = usuarioService.logarUsuario(requestCerto);
            assertNotNull(response);
            assertEquals("token-vitao", response.getToken());
        }

        @Test
        @DisplayName("Login com sucesso")
        void logarUsuarioComSucesso() {
            LoginRequest request = new LoginRequest();
            request.setEmail("marcos@gmail.com");
            request.setSenha("teste123*");

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(1L);
            tipo.setCargo("root");

            Usuario usuario = new Usuario();
            usuario.setId(1L);
            usuario.setNome("Marcos Vinicius");
            usuario.setEmail("marcos@gmail.com");
            usuario.setTelefone("11935234123");
            usuario.setSenha("luanWasHere");
            usuario.setTipoUsuario(tipo);

            UsuarioDetalhesDto detalhes = new UsuarioDetalhesDto(usuario);

            Authentication autenticacao = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(autenticacao);
            when(autenticacao.getPrincipal()).thenReturn(detalhes);
            when(gerenciadorTokenJwt.generateToken(detalhes)).thenReturn("token-vitao");

            UsuarioTokenResponse response = usuarioService.logarUsuario(request);

            assertNotNull(response);
            assertEquals("token-vitao", response.getToken());
            assertEquals("marcos@gmail.com", response.getEmail());
            assertEquals("root", response.getCargo());
        }

        @Test
        @DisplayName("Login com sucesso - principal como String")
        void logarUsuarioComSucessoPrincipalString() {
            LoginRequest request = new LoginRequest();
            request.setEmail("marcos@gmail.com");
            request.setSenha("teste123*");

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(1L);
            tipo.setCargo("root");

            Usuario usuario = new Usuario();
            usuario.setId(1L);
            usuario.setNome("Marcos Vinicius");
            usuario.setEmail("marcos@gmail.com");
            usuario.setTelefone("11935234123");
            usuario.setSenha("luanWasHere");
            usuario.setTipoUsuario(tipo);

            Authentication autenticacao = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(autenticacao);

            // principal vem como String
            when(autenticacao.getPrincipal()).thenReturn("marcos@gmail.com");

            when(usuarioRepository.findByEmail("marcos@gmail.com")).thenReturn(Optional.of(usuario));

            when(gerenciadorTokenJwt.generateToken(any(UsuarioDetalhesDto.class))).thenReturn("token-vitao");

            UsuarioTokenResponse response = usuarioService.logarUsuario(request);

            assertNotNull(response);
            assertEquals("token-vitao", response.getToken());
            assertEquals("marcos@gmail.com", response.getEmail());
            assertEquals("root", response.getCargo());
        }

        @Test
        @DisplayName("Login Falho - Autenticação")
        void logarUsuarioFalhoSemAutenticacao() {
            LoginRequest request = new LoginRequest();
            request.setEmail("marcos@gmail.com");
            request.setSenha("teste123*");

            Authentication autenticacao = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(autenticacao);

            when(autenticacao.getPrincipal()).thenReturn("marcos@gmail.com");

            when(usuarioRepository.findByEmail("marcos@gmail.com")).thenReturn(Optional.empty());
            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.logarUsuario(request)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class BuscarUsuario {

        @Test
        @DisplayName("Buscar Todos os Usuários")
        void buscarUsuarios() {
            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(3L);
            tipo.setCargo("professor");

            Usuario usuario1 = new Usuario();
            usuario1.setId(1L);
            usuario1.setNome("Marcos Vinicius");
            usuario1.setEmail("marcos@gmail.com");
            usuario1.setTelefone("11935234123");
            usuario1.setTipoUsuario(tipo);

            Usuario usuario2 = new Usuario();
            usuario2.setId(2L);
            usuario2.setNome("João Silva");
            usuario2.setEmail("joao@gmail.com");
            usuario2.setTelefone("11912345678");
            usuario2.setTipoUsuario(tipo);

            Pageable pageable = PageRequest.of(1, 5);
            when(usuarioRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(usuario1, usuario2), pageable, 12));

            PaginaResponse<UsuarioResponse> response =
                    usuarioService.buscarUsuarios(pageable, null, null);

            assertNotNull(response);
            assertEquals(2, response.content().size());
            assertEquals(1, response.page());
            assertEquals(5, response.size());
            assertEquals(12, response.totalElements());
            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(usuarioRepository).findAll(any(Specification.class), pageableCaptor.capture());
            assertEquals(1, pageableCaptor.getValue().getPageNumber());
            assertEquals(5, pageableCaptor.getValue().getPageSize());
            assertNotNull(pageableCaptor.getValue().getSort().getOrderFor("id"));
        }

        @Test
        @DisplayName("Buscar Usuário por ID")
        void buscarUsuariosPorId() {
            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(3L);
            tipo.setCargo("professor");

            Usuario usuario = new Usuario();
            usuario.setId(2L);
            usuario.setNome("João Silva");
            usuario.setEmail("joao@gmail.com");
            usuario.setTelefone("11912345678");
            usuario.setTipoUsuario(tipo);

            when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

            UsuarioResponse response = usuarioService.buscarUsuarioPorId(2L);

            assertNotNull(response);
            assertEquals("João Silva", response.getNome());
        }

        @Test
        @DisplayName("Buscar Falho Usuário por ID")
        void buscarUsuariosPorIdFalho() {
            when(usuarioRepository.findById(3L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.buscarUsuarioPorId(3L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Buscar Usuário por Nome")
        void buscarUsuarioPorNome() {
            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(1L);
            tipo.setCargo("root");

            Usuario usuario = new Usuario();
            usuario.setId(1L);
            usuario.setNome("Marcos Vinicius");
            usuario.setEmail("marcos@gmail.com");
            usuario.setTelefone("11935234123");
            usuario.setTipoUsuario(tipo);

            Pageable pageable = PageRequest.of(0, 10);
            when(usuarioRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(usuario), pageable, 1));

            PaginaResponse<UsuarioResponse> response =
                    usuarioService.buscarUsuarios(pageable, "Marcos", null);

            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals("Marcos Vinicius", response.content().get(0).getNome());
            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
            verify(usuarioRepository).findAll(any(Specification.class), pageableCaptor.capture());
            assertEquals(0, pageableCaptor.getValue().getPageNumber());
            assertEquals(10, pageableCaptor.getValue().getPageSize());
        }
    }

    @Nested
    class AtualizarUsuario {

        @Test
        @DisplayName("Atualizar Usuário Comum com Sucesso")
        void atualizarUsuarioComum() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(1L);
            request.setNome("Marcos Atualizado");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(1L);
            tipo.setCargo("root");

            Usuario usuarioExistente = new Usuario();
            usuarioExistente.setId(1L);
            usuarioExistente.setNome("Marcos Vinicius");
            usuarioExistente.setEmail("marcos@gmail.com");
            usuarioExistente.setTelefone("11935234123");
            usuarioExistente.setTipoUsuario(tipo);

            Usuario usuarioAtualizado = new Usuario();
            usuarioAtualizado.setId(1L);
            usuarioAtualizado.setNome("Marcos Atualizado");
            usuarioAtualizado.setEmail("marcos@gmail.com");
            usuarioAtualizado.setTelefone("11935234123");
            usuarioAtualizado.setTipoUsuario(tipo);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipo));
            when(passwordEncoder.encode("teste123*")).thenReturn("luanWasHere");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

            UsuarioResponse response = usuarioService.atualizarUsuarioPorId(1L, request);

            assertNotNull(response);
            assertEquals("Marcos Atualizado", response.getNome());
        }

        @Test
        @DisplayName("Atualizar Aluno com Condomínio com Sucesso")
        void atualizarUsuarioAluno() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("João Atualizado");
            request.setEmail("joao@gmail.com");
            request.setTelefone("11912345678");
            request.setSenha("teste123*");
            request.setCondominio(1L);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Condominio condominio = new Condominio();
            condominio.setId(1L);
            condominio.setNome("Condomínio LIV");

            Usuario usuarioExistente = new Usuario();
            usuarioExistente.setId(2L);
            usuarioExistente.setNome("João Silva");
            usuarioExistente.setEmail("joao@gmail.com");
            usuarioExistente.setTelefone("11912345678");
            usuarioExistente.setTipoUsuario(tipo);

            Usuario usuarioAtualizado = new Usuario();
            usuarioAtualizado.setId(2L);
            usuarioAtualizado.setNome("João Atualizado");
            usuarioAtualizado.setEmail("joao@gmail.com");
            usuarioAtualizado.setTelefone("11912345678");
            usuarioAtualizado.setTipoUsuario(tipo);
            usuarioAtualizado.setCondominio(condominio);

            when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuarioExistente));
            when(tipoUsuarioRepository.findById(4L)).thenReturn(Optional.of(tipo));
            when(condominioRepository.findById(1L)).thenReturn(Optional.of(condominio));
            when(passwordEncoder.encode("teste123*")).thenReturn("luanWasHere");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

            UsuarioResponse response = usuarioService.atualizarUsuarioPorId(2L, request);

            assertNotNull(response);
            assertEquals("João Atualizado", response.getNome());
            assertNotNull(response.getCondominio());
        }

        @Test
        @DisplayName("Atualizar Usuário não encontrado")
        void atualizarUsuarioNaoEncontrado() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(1L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.atualizarUsuarioPorId(99L, request)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Usuário - Tipo não encontrado")
        void atualizarUsuarioTipoNaoEncontrado() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(99L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(1L);
            tipo.setCargo("root");

            Usuario usuarioExistente = new Usuario();
            usuarioExistente.setId(1L);
            usuarioExistente.setNome("Marcos Vinicius");
            usuarioExistente.setEmail("marcos@gmail.com");
            usuarioExistente.setTelefone("11935234123");
            usuarioExistente.setTipoUsuario(tipo);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
            when(tipoUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.atualizarUsuarioPorId(1L, request)
            );
            assertEquals(404, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Aluno sem Condomínio")
        void atualizarAlunoSemCondominio() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("João Silva");
            request.setEmail("joao@gmail.com");
            request.setTelefone("11912345678");
            request.setSenha("teste123*");

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario usuarioExistente = new Usuario();
            usuarioExistente.setId(2L);
            usuarioExistente.setNome("João Silva");
            usuarioExistente.setEmail("joao@gmail.com");
            usuarioExistente.setTelefone("11912345678");
            usuarioExistente.setTipoUsuario(tipo);

            when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuarioExistente));
            when(tipoUsuarioRepository.findById(4L)).thenReturn(Optional.of(tipo));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.atualizarUsuarioPorId(2L, request)
            );
            assertEquals(400, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Atualizar Aluno - Condomínio não encontrado")
        void atualizarAlunoCondominioNaoEncontrado() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("João Silva");
            request.setEmail("joao@gmail.com");
            request.setTelefone("11912345678");
            request.setSenha("teste123*");
            request.setCondominio(99L);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(4L);
            tipo.setCargo("aluno");

            Usuario usuarioExistente = new Usuario();
            usuarioExistente.setId(2L);
            usuarioExistente.setNome("João Silva");
            usuarioExistente.setEmail("joao@gmail.com");
            usuarioExistente.setTelefone("11912345678");
            usuarioExistente.setTipoUsuario(tipo);

            when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuarioExistente));
            when(tipoUsuarioRepository.findById(4L)).thenReturn(Optional.of(tipo));
            when(condominioRepository.findById(99L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.atualizarUsuarioPorId(2L, request)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    private void mockAutenticacao(String email, String role) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                email,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Usuario criarUsuarioComCargo(Long id, String cargo) {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setCargo(cargo);
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Usuário " + id);
        usuario.setEmail("usuario" + id + "@gmail.com");
        usuario.setTipoUsuario(tipo);
        return usuario;
    }

    @Nested
    class DeletarUsuario {

        @Test
        @DisplayName("Root deleta usuário sem histórico")
        void deletarUsuario() {
            mockAutenticacao("root@gmail.com", "ROLE_ROOT");
            Usuario usuario = criarUsuarioComCargo(1L, "professor");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(agendamentoRepository.existeAgendamentoDoUsuario(1L)).thenReturn(false);
            when(saldoRepository.existsByAlunoId(1L)).thenReturn(false);

            usuarioService.deletarUsuarioPorId(1L);
            verify(usuarioRepository).delete(usuario);
        }

        @Test
        @DisplayName("Administracao não pode excluir usuários definitivamente")
        void administracaoNaoPodeExcluir() {
            mockAutenticacaoAdmin();
            Usuario usuario = criarUsuarioComCargo(1L, "professor");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.deletarUsuarioPorId(1L));

            assertEquals(403, exception.getStatusCode().value());
            verify(usuarioRepository, never()).delete(any(Usuario.class));
        }

        @Test
        @DisplayName("Root não pode excluir usuário com agendamentos")
        void naoExcluiUsuarioComAgendamentos() {
            mockAutenticacao("root@gmail.com", "ROLE_ROOT");
            Usuario usuario = criarUsuarioComCargo(1L, "professor");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(agendamentoRepository.existeAgendamentoDoUsuario(1L)).thenReturn(true);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.deletarUsuarioPorId(1L));

            assertEquals(409, exception.getStatusCode().value());
            verify(usuarioRepository, never()).delete(any(Usuario.class));
        }

        @Test
        @DisplayName("Root não pode excluir aluno com saldo")
        void naoExcluiUsuarioComSaldo() {
            mockAutenticacao("root@gmail.com", "ROLE_ROOT");
            Usuario usuario = criarUsuarioComCargo(1L, "aluno");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(agendamentoRepository.existeAgendamentoDoUsuario(1L)).thenReturn(false);
            when(saldoRepository.existsByAlunoId(1L)).thenReturn(true);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.deletarUsuarioPorId(1L));

            assertEquals(409, exception.getStatusCode().value());
            verify(usuarioRepository, never()).delete(any(Usuario.class));
        }

        @Test
        @DisplayName("Root não pode excluir o próprio usuário")
        void rootNaoExcluiASiMesmo() {
            mockAutenticacao("usuario1@gmail.com", "ROLE_ROOT");
            Usuario usuario = criarUsuarioComCargo(1L, "root");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.deletarUsuarioPorId(1L));

            assertEquals(403, exception.getStatusCode().value());
            verify(usuarioRepository, never()).delete(any(Usuario.class));
        }

        @Test
        @DisplayName("Falha Deletar Usuário")
        void deletarUsuarioFalho() {
            when(usuarioRepository.findById(300L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.deletarUsuarioPorId(300L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class ArquivarUsuario {

        @Test
        @DisplayName("Arquivar usuário sem agendamentos futuros")
        void arquivarUsuario() {
            mockAutenticacaoAdmin();
            Usuario usuario = criarUsuarioComCargo(1L, "professor");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(agendamentoRepository.contarAgendamentosFuturosDoUsuario(eq(1L), any(LocalDate.class), anyList()))
                    .thenReturn(0L);

            usuarioService.arquivarUsuarioPorId(1L);

            assertTrue(usuario.isArquivado());
            verify(usuarioRepository).save(usuario);
        }

        @Test
        @DisplayName("Não arquiva usuário com agendamentos futuros")
        void naoArquivaComAgendamentosFuturos() {
            mockAutenticacaoAdmin();
            Usuario usuario = criarUsuarioComCargo(1L, "aluno");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(agendamentoRepository.contarAgendamentosFuturosDoUsuario(eq(1L), any(LocalDate.class), anyList()))
                    .thenReturn(2L);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.arquivarUsuarioPorId(1L));

            assertEquals(409, exception.getStatusCode().value());
            assertTrue(exception.getReason().contains("2 agendamento(s)"));
            assertFalse(usuario.isArquivado());
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Não arquiva usuário já arquivado")
        void naoArquivaUsuarioJaArquivado() {
            mockAutenticacaoAdmin();
            Usuario usuario = criarUsuarioComCargo(1L, "professor");
            usuario.setArquivadoEm(LocalDateTime.now());
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.arquivarUsuarioPorId(1L));

            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Root não pode ser arquivado")
        void rootNaoPodeSerArquivado() {
            mockAutenticacao("outro-root@gmail.com", "ROLE_ROOT");
            Usuario usuario = criarUsuarioComCargo(1L, "root");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.arquivarUsuarioPorId(1L));

            assertEquals(403, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Administracao não pode arquivar outro administrador")
        void administracaoNaoArquivaAdministracao() {
            mockAutenticacaoAdmin();
            Usuario usuario = criarUsuarioComCargo(1L, "administracao");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.arquivarUsuarioPorId(1L));

            assertEquals(403, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Root pode arquivar administrador")
        void rootArquivaAdministracao() {
            mockAutenticacao("root@gmail.com", "ROLE_ROOT");
            Usuario usuario = criarUsuarioComCargo(1L, "administracao");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(agendamentoRepository.contarAgendamentosFuturosDoUsuario(eq(1L), any(LocalDate.class), anyList()))
                    .thenReturn(0L);

            usuarioService.arquivarUsuarioPorId(1L);

            assertTrue(usuario.isArquivado());
        }

        @Test
        @DisplayName("Usuário não pode arquivar a si mesmo")
        void naoArquivaASiMesmo() {
            mockAutenticacao("usuario1@gmail.com", "ROLE_ROOT");
            Usuario usuario = criarUsuarioComCargo(1L, "administracao");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.arquivarUsuarioPorId(1L));

            assertEquals(403, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Arquivar usuário inexistente")
        void arquivarUsuarioInexistente() {
            when(usuarioRepository.findById(300L)).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.arquivarUsuarioPorId(300L));

            assertEquals(404, exception.getStatusCode().value());
        }
    }

    @Nested
    class DesarquivarUsuario {

        @Test
        @DisplayName("Desarquivar usuário arquivado")
        void desarquivarUsuario() {
            mockAutenticacaoAdmin();
            Usuario usuario = criarUsuarioComCargo(1L, "aluno");
            usuario.setArquivadoEm(LocalDateTime.now());
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            usuarioService.desarquivarUsuarioPorId(1L);

            assertFalse(usuario.isArquivado());
            verify(usuarioRepository).save(usuario);
        }

        @Test
        @DisplayName("Não desarquiva usuário ativo")
        void naoDesarquivaUsuarioAtivo() {
            mockAutenticacaoAdmin();
            Usuario usuario = criarUsuarioComCargo(1L, "aluno");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.desarquivarUsuarioPorId(1L));

            assertEquals(409, exception.getStatusCode().value());
        }
    }

    @Nested
    class RegrasUsuarioArquivado {

        @Test
        @DisplayName("Login de usuário arquivado retorna 403")
        void loginUsuarioArquivado() {
            LoginRequest request = new LoginRequest();
            request.setEmail("arquivado@gmail.com");
            request.setSenha("teste123*");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new DisabledException("Usuário arquivado"));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.logarUsuario(request));

            assertEquals(403, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Não atualiza usuário arquivado")
        void naoAtualizaUsuarioArquivado() {
            Usuario usuario = criarUsuarioComCargo(1L, "professor");
            usuario.setArquivadoEm(LocalDateTime.now());
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.atualizarUsuarioPorId(1L, new UsuarioRequest()));

            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Cadastro com email de usuário arquivado sugere desarquivar")
        void cadastroComEmailDeArquivado() {
            mockAutenticacaoAdmin();
            UsuarioRequest request = new UsuarioRequest();
            request.setEmail("usuario1@gmail.com");
            Usuario arquivado = criarUsuarioComCargo(1L, "professor");
            arquivado.setArquivadoEm(LocalDateTime.now());
            when(usuarioRepository.existsByEmail("usuario1@gmail.com")).thenReturn(true);
            when(usuarioRepository.findByEmail("usuario1@gmail.com")).thenReturn(Optional.of(arquivado));

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.criarUsuario(request));

            assertEquals(409, exception.getStatusCode().value());
            assertTrue(exception.getReason().contains("arquivado"));
        }

        @Test
        @DisplayName("Status de busca inválido retorna 400")
        void statusBuscaInvalido() {
            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.buscarUsuarios(PageRequest.of(0, 10), null, null, null, null, null, "qualquer"));

            assertEquals(400, exception.getStatusCode().value());
        }
    }
}