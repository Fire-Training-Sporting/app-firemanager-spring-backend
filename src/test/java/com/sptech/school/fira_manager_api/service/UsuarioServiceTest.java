package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.config.GerenciadorTokenJwt;
import com.sptech.school.fira_manager_api.dto.UsuarioDetalhesDto;
import com.sptech.school.fira_manager_api.dto.requests.usuario.LoginRequest;
import com.sptech.school.fira_manager_api.dto.requests.usuario.UsuarioRequest;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioTokenResponse;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.model.TipoUsuario;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import com.sptech.school.fira_manager_api.repository.TipoUsuarioRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private TipoUsuarioRepository tipoUsuarioRepository;
    @Mock private CondominioRepository condominioRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private UsuarioService usuarioService;

    @Nested
    class CriarUsuario {

        @Test
        @DisplayName("Cadastrar Funcionário Sucesso ")
        void cadastrarUsuarioFuncionario() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(1L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            when(usuarioRepository.existsByNome("Marcos Vinicius")).thenReturn(false);
            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(false);
            when(usuarioRepository.existsByTelefone("11935234123")).thenReturn(false);

            TipoUsuario tipo = new TipoUsuario();
            tipo.setId(1L);
            tipo.setCargo("root");
            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipo));

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
        @DisplayName("Cadastrar Aluno Sucesso")
        void cadastrarUsuarioAluno() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(1L);

            when(usuarioRepository.existsByNome("Marcos Vinicius")).thenReturn(false);
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
        @DisplayName("Cadastro Falho - Nome Cadastrado")
        void cadastrarUsuarioNomeRepetido() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(1L);

            when(usuarioRepository.existsByNome("Marcos Vinicius")).thenReturn(true);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.criarUsuario(request)
            );
            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Cadastro Falho - Email Cadastrado")
        void cadastrarUsuarioEmailRepetido() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(1L);

            when(usuarioRepository.existsByNome("Marcos Vinicius")).thenReturn(false);
            when(usuarioRepository.existsByEmail("marcos@gmail.com")).thenReturn(true);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.criarUsuario(request)
            );
            assertEquals(409, exception.getStatusCode().value());
        }

        @Test
        @DisplayName("Cadastro Falho - Telefone Cadastrado")
        void cadastrarUsuarioTelefoneRepetido() {
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(1L);

            when(usuarioRepository.existsByNome("Marcos Vinicius")).thenReturn(false);
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
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(99L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            when(usuarioRepository.existsByNome("Marcos Vinicius")).thenReturn(false);
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
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");
            request.setCondominio(100L);

            when(usuarioRepository.existsByNome("Marcos Vinicius")).thenReturn(false);
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
            UsuarioRequest request = new UsuarioRequest();
            request.setTipoUsuario(4L);
            request.setNome("Marcos Vinicius");
            request.setEmail("marcos@gmail.com");
            request.setTelefone("11935234123");
            request.setSenha("teste123*");

            when(usuarioRepository.existsByNome("Marcos Vinicius")).thenReturn(false);
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

            when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

            List<UsuarioResponse> response = usuarioService.buscarUsuarios();

            assertNotNull(response);
            assertEquals(2, response.size());
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

            when(usuarioRepository.findByNomeContainingIgnoreCase("Marcos"))
                    .thenReturn(List.of(usuario));

            List<UsuarioResponse> response = usuarioService.buscarUsuarioPorNome("Marcos");

            assertNotNull(response);
            assertEquals(1, response.size());
            assertEquals("Marcos Vinicius", response.get(0).getNome());
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

    @Nested
    class DeletarUsuario {

        @Test
        @DisplayName("Deletar Usuário")
        void deletarUsuario() {
            when(usuarioRepository.existsById(1L)).thenReturn(true);

            usuarioService.deletarUsuarioPorId(1L);
            verify(usuarioRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Falha Deletar Usuário")
        void deletarUsuarioFalho() {
            when(usuarioRepository.existsById(300L)).thenReturn(false);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> usuarioService.deletarUsuarioPorId(300L)
            );
            assertEquals(404, exception.getStatusCode().value());
        }
    }
}