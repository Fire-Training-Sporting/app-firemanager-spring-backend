package com.sptech.school.fira_manager_api.service;

import com.sptech.school.fira_manager_api.dto.requests.condominio.CondominioRequest;
import com.sptech.school.fira_manager_api.dto.responses.PaginaResponse;
import com.sptech.school.fira_manager_api.dto.responses.condominio.CondominioResponse;
import com.sptech.school.fira_manager_api.mapper.condominio.CondominioMapper;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.repository.CondominioRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CondominioService {

    private final CondominioRepository condominioRepository;

    public CondominioService(CondominioRepository condominioRepository) {
        this.condominioRepository = condominioRepository;
    }

    public CondominioResponse adicionarNovoCondominio(CondominioRequest dto) {
        Condominio condominioNovo = CondominioMapper.toEntity(dto);
        return CondominioMapper.toResponse(condominioRepository.save(condominioNovo));
    }

    public List<CondominioResponse> obterCondominios() {
        return condominioRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(CondominioMapper::toResponse)
                .toList();
    }

    public PaginaResponse<CondominioResponse> obterCondominios(Pageable pageable, String campo, String busca) {
        String campoBusca = campo == null || campo.isBlank() ? "nome" : campo;
        String termo = busca == null || busca.isBlank() ? null : busca.trim().toLowerCase(Locale.ROOT);
        List<String> camposPermitidos = List.of("id", "nome", "cep", "logradouro", "numero", "cidade", "bairro");

        if (termo != null && !camposPermitidos.contains(campoBusca)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de busca inválido");
        }

        Specification<Condominio> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (termo != null) {
                String padrao = "%" + termo + "%";
                switch (campoBusca) {
                    case "id" -> predicates.add(criteriaBuilder.like(root.get("id").as(String.class), padrao));
                    case "logradouro" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("rua")), padrao));
                        case "cep" -> {
                        String cepBusca = termo.replaceAll("\\D", "");
                        if (cepBusca.isEmpty()) {
                            predicates.add(criteriaBuilder.disjunction());
                        } else {
                            predicates.add(criteriaBuilder.like(
                                criteriaBuilder.function("replace", String.class,
                                    root.get("cep"), criteriaBuilder.literal("-"), criteriaBuilder.literal("")),
                                "%" + cepBusca + "%"));
                        }
                        }
                    case "nome", "numero", "cidade", "bairro" -> predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(campoBusca)), padrao));
                    default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de busca inválido");
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageablePorId = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "id"));
        Page<CondominioResponse> pagina = condominioRepository.findAll(specification, pageablePorId)
                .map(this::toResponse);
        return PaginaResponse.from(pagina);
    }

    public CondominioResponse atualizarCondominio(Long id, CondominioRequest dto) {
        Condominio condominio = condominioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "O Condominio solicitado não existe."));

        condominio.setNome(dto.getNome());
        condominio.setCidade(dto.getCidade());
        condominio.setBairro(dto.getBairro());
        condominio.setLogradouro(dto.getLogradouro());
        condominio.setNumero(dto.getNumero());
        condominio.setCep(dto.getCep());

        return CondominioMapper.toResponse(condominioRepository.save(condominio));
    }

    public void deletarCondominio(Long id) {
        if (!condominioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O Condominio não existe");
        }
        condominioRepository.deleteById(id);
    }
}
