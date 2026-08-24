package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.requests.condominio.CondominioRequest;
import com.sptech.school.fira_manager_api.dto.responses.condominio.CondominioResponse;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
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
class CondominioServiceTest {

    @Mock
    private CondominioRepository condominioRepository;

    @InjectMocks
    private CondominioService condominioService;

    @Test
    void deveAdicionarNovoCondominio() {

        CondominioRequest request = new CondominioRequest();
        request.setNome("Condomínio Teste");
        request.setCidade("São Paulo");
        request.setBairro("Centro");
        request.setLogradouro("Rua A");
        request.setNumero("100");
        request.setCep("01000-000");

        Condominio salvo = new Condominio();
        salvo.setId(1L);
        salvo.setNome(request.getNome());
        salvo.setCidade(request.getCidade());
        salvo.setBairro(request.getBairro());
        salvo.setLogradouro(request.getLogradouro());
        salvo.setNumero(request.getNumero());
        salvo.setCep(request.getCep());

        when(condominioRepository.save(any(Condominio.class)))
                .thenReturn(salvo);

        CondominioResponse response =
                condominioService.adicionarNovoCondominio(request);

        assertNotNull(response);
        assertEquals("Condomínio Teste", response.getNome());

        verify(condominioRepository).save(any(Condominio.class));
    }

    @Test
    void deveObterCondominios() {

        Condominio c1 = new Condominio();
        c1.setId(1L);
        c1.setNome("Condomínio A");

        Condominio c2 = new Condominio();
        c2.setId(2L);
        c2.setNome("Condomínio B");

        when(condominioRepository.findAll())
                .thenReturn(List.of(c1, c2));

        List<CondominioResponse> response =
                condominioService.obterCondominios();

        assertEquals(2, response.size());

        verify(condominioRepository).findAll();
    }

    @Test
    void deveAtualizarCondominio() {

        Long id = 1L;

        Condominio existente = new Condominio();
        existente.setId(id);
        existente.setNome("Antigo");

        CondominioRequest request = new CondominioRequest();
        request.setNome("Novo Nome");
        request.setCidade("São Paulo");
        request.setBairro("Centro");
        request.setLogradouro("Rua Nova");
        request.setNumero("123");
        request.setCep("01000-000");

        when(condominioRepository.findById(id))
                .thenReturn(Optional.of(existente));

        when(condominioRepository.save(any(Condominio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CondominioResponse response =
                condominioService.atualizarCondominio(id, request);

        assertEquals("Novo Nome", response.getNome());

        verify(condominioRepository).findById(id);
        verify(condominioRepository).save(any(Condominio.class));
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarCondominioInexistente() {

        Long id = 99L;

        when(condominioRepository.findById(id))
                .thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> condominioService.atualizarCondominio(
                                id,
                                new CondominioRequest()
                        )
                );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void deveDeletarCondominio() {

        Long id = 1L;

        when(condominioRepository.existsById(id))
                .thenReturn(true);

        condominioService.deletarCondominio(id);

        verify(condominioRepository).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoDeletarCondominioInexistente() {

        Long id = 99L;

        when(condominioRepository.existsById(id))
                .thenReturn(false);

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> condominioService.deletarCondominio(id)
                );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(condominioRepository, never())
                .deleteById(anyLong());
    }
}