package com.sptech.school.fira_manager_api.dto.responses;

import java.util.List;

import org.springframework.data.domain.Page;

public record PaginaResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> PaginaResponse<T> from(Page<T> resultado) {
        return new PaginaResponse<>(
                resultado.getContent(),
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.isFirst(),
                resultado.isLast()
        );
    }
}