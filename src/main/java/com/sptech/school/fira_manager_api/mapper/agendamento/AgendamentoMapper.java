package com.sptech.school.fira_manager_api.mapper.agendamento;

import com.sptech.school.fira_manager_api.dto.responses.agendamento.AgendamentoResponse;
import com.sptech.school.fira_manager_api.dto.responses.condominio.CondominioResponse;
import com.sptech.school.fira_manager_api.dto.responses.saldo.SaldoResponse;
import com.sptech.school.fira_manager_api.dto.responses.servico.ServicoResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.ProfessorResponse;
import com.sptech.school.fira_manager_api.dto.responses.usuario.UsuarioResponse;
import com.sptech.school.fira_manager_api.mapper.saldo.SaldoMapper;
import com.sptech.school.fira_manager_api.model.Agendamento;
import com.sptech.school.fira_manager_api.model.Condominio;
import com.sptech.school.fira_manager_api.model.Saldo;
import com.sptech.school.fira_manager_api.model.Servico;
import com.sptech.school.fira_manager_api.model.Usuario;

public class AgendamentoMapper {

    private AgendamentoMapper() {}

    private static ProfessorResponse toProfessorResponse(Usuario usuario) {
        if (usuario == null) return null;

        return new ProfessorResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }

    private static ServicoResponse toServicoResponse(Servico servico) {
        if (servico == null) return null;

        return new ServicoResponse(
                servico.getId(),
                servico.getNome()
        );
    }

    private static UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }

    private static CondominioResponse toCondominioResponse(Condominio condominio) {
        if (condominio == null) return null;

        return new CondominioResponse(
                condominio.getNome(),
                condominio.getCidade(),
                condominio.getBairro(),
                condominio.getLogradouro(),
                condominio.getNumero()
        );
    }

    public static AgendamentoResponse toResponse(Agendamento agendamento, Saldo saldo) {
        ProfessorResponse professorResponse = toProfessorResponse(agendamento.getProfessor());
        ServicoResponse servicoResponse = toServicoResponse(agendamento.getServico());
        SaldoResponse saldoResponse = SaldoMapper.toResponse(saldo);
        UsuarioResponse usuarioResponse = toUsuarioResponse(agendamento.getAluno());
        CondominioResponse condominioResponse = toCondominioResponse(agendamento.getCondominio());

        AgendamentoResponse response = new AgendamentoResponse(
                agendamento.getId(),
                usuarioResponse,
                saldoResponse,
                professorResponse,
                toProfessorResponse(agendamento.getAuxiliar()),
                servicoResponse,
                condominioResponse,
                agendamento.getData(),
                agendamento.getHoraInicio(),
                agendamento.getHoraFim(),
                agendamento.getObservacao(),
                agendamento.getCriadoEm(),
                agendamento.getAtualizadoEm(),
                agendamento.getStatus()
        );

        response.setRebatedor(toProfessorResponse(agendamento.getRebatedor()));

        return response;
    }
}