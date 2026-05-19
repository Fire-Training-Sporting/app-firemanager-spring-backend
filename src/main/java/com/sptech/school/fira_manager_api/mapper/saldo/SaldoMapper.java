package com.sptech.school.fira_manager_api.mapper.saldo;
import com.sptech.school.fira_manager_api.dto.responses.saldo.SaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Servico;

public class SaldoMapper {

    private SaldoMapper() {}

    private static ServicoResponse toServicoResponse(Servico servico) {
        if (servico == null) return null;

        return new ServicoResponse(
                servico.getId(),
                servico.getNome()
        );
    }

    public static SaldoResponse toResponse(Saldo saldo) {
        if (saldo == null) return null;

        return new SaldoResponse(
                saldo.getQuantidade(),
                toServicoResponse(saldo.getServico())
        );
    }
}