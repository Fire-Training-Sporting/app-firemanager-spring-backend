package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.requests.saldo.SaldoRequest;
import com.sptech.school.fira_manager_api.model.*;
import com.sptech.school.fira_manager_api.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaldoServiceTest {

    @Mock
    private SaldoRepository saldoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private SaldoTransacaoRepository saldoTransacaoRepository;

    @InjectMocks
    private SaldoService service;

    // -----------------------------
    // CRIAR SALDO
    // -----------------------------
    @Test
    void deveCriarSaldoComSucesso() {
        SaldoRequest dto = new SaldoRequest();
        dto.setAluno(1L);
        dto.setServico(1L);
        dto.setQuantidade(10.0);

        Usuario aluno = new Usuario();
        aluno.setId(1L);

        Servico servico = new Servico();
        servico.setId(1L);

        Saldo saldo = new Saldo();
        saldo.setId(1L);
        saldo.setQuantidade(0.0);
        saldo.setAluno(aluno);
        saldo.setServico(servico);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L))
                .thenReturn(Optional.empty());
        when(saldoRepository.save(any())).thenReturn(saldo);

        service.criarSaldo(dto);

        verify(saldoRepository, atLeastOnce()).save(any(Saldo.class));
        verify(saldoTransacaoRepository, atLeastOnce()).save(any());
    }

    @Test
    void deveAdicionarSaldoEmSaldoExistente() {
        SaldoRequest dto = new SaldoRequest();
        dto.setAluno(1L);
        dto.setServico(1L);
        dto.setQuantidade(5.0);

        Usuario aluno = new Usuario();
        Servico servico = new Servico();

        Saldo saldoExistente = new Saldo();
        saldoExistente.setId(1L);
        saldoExistente.setQuantidade(10.0);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(saldoRepository.findByAlunoIdAndServicoId(1L, 1L))
                .thenReturn(Optional.of(saldoExistente));

        service.criarSaldo(dto);

        verify(saldoRepository, atLeastOnce()).save(any(Saldo.class));
    }

    // -----------------------------
    // LISTAR
    // -----------------------------
    @Test
    void deveListarSaldos() {
        when(saldoRepository.findAll()).thenReturn(List.of());

        var result = service.listarSaldos();

        assertTrue(result.isEmpty());
        verify(saldoRepository).findAll();
    }

    // -----------------------------
    // LISTAR POR ID
    // -----------------------------
    @Test
    void deveBuscarSaldoPorId() {
        Saldo saldo = new Saldo();
        saldo.setId(1L);

        when(saldoRepository.findById(1L))
                .thenReturn(Optional.of(saldo));

        var result = service.listarSaldoPorId(1L);

        assertNotNull(result);
        verify(saldoRepository).findById(1L);
    }

    @Test
    void deveLancarErroQuandoSaldoNaoExiste() {
        when(saldoRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.listarSaldoPorId(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // -----------------------------
    // ATUALIZAR
    // -----------------------------
    @Test
    void deveAtualizarSaldoComSucesso() {
        SaldoRequest dto = new SaldoRequest();
        dto.setAluno(1L);
        dto.setServico(1L);
        dto.setQuantidade(10.0);

        Saldo saldo = new Saldo();
        saldo.setId(1L);
        saldo.setQuantidade(0.0);

        Usuario aluno = new Usuario();
        Servico servico = new Servico();

        when(saldoRepository.findById(1L)).thenReturn(Optional.of(saldo));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

        service.atualizarSaldoPorId(dto, 1L);

        verify(saldoRepository, atLeastOnce()).save(any());
    }

    // -----------------------------
    // DELETAR
    // -----------------------------
    @Test
    void deveDeletarSaldo() {
        when(saldoRepository.existsById(1L)).thenReturn(true);

        service.deletarSaldoPorId(1L);

        verify(saldoRepository).deleteById(1L);
    }

    @Test
    void deveLancarErroAoDeletarSaldoInexistente() {
        when(saldoRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.deletarSaldoPorId(1L)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    // -----------------------------
    // EXPIRAR SALDOS (SCHEDULED)
    // -----------------------------
    @Test
    void deveExpirarSaldos() {
        Saldo saldo = new Saldo();
        saldo.setQuantidade(10.0);

        SaldoTransacao transacao = mock(SaldoTransacao.class);
        when(transacao.getSaldo()).thenReturn(saldo);
        when(transacao.getQuantidadeRestante()).thenReturn(5.0);

        when(saldoTransacaoRepository
                .findByDataExpiracaoBeforeAndQuantidadeRestanteGreaterThan(any(), anyDouble()))
                .thenReturn(List.of(transacao));

        service.expirarSaldos();

        verify(saldoTransacaoRepository).save(any());
        verify(saldoRepository).save(any());
    }

    // -----------------------------
    // BUSCAR SALDO PROFESSOR
    // -----------------------------
    @Test
    void deveBuscarSaldoProfessorComSucesso() {
        Usuario professor = new Usuario();
        professor.setId(1L);

        TipoUsuario tipo = mock(TipoUsuario.class);
        when(tipo.getCargo()).thenReturn("professor");
        professor.setTipoUsuario(tipo);

        Servico servico = new Servico();
        servico.setId(1L);
        servico.setNome("Musculação");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(servicoRepository.findAll()).thenReturn(List.of(servico));

        when(agendamentoRepository.countByProfessorIdAndServicoIdAndStatus(anyLong(), anyLong(), anyString()))
                .thenReturn(2L);

        when(agendamentoRepository.countByAuxiliarIdAndServicoIdAndStatus(anyLong(), anyLong(), anyString()))
                .thenReturn(1L);

        var result = service.buscarSaldoProfessorPorId(1L);

        assertNotNull(result);
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void deveLancarErroQuandoNaoEhProfessor() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        TipoUsuario tipo = mock(TipoUsuario.class);
        when(tipo.getCargo()).thenReturn("aluno");
        usuario.setTipoUsuario(tipo);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.buscarSaldoProfessorPorId(1L)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }
}