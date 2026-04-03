package com.sptech.school.fira_manager_api.observer;

import com.sptech.school.fira_manager_api.dto.Agendamento;

// Essa interface faz com que todos os observer quem receberá atualizações)
// sejam obrigados a implementar e sobreescrever o metodo update.

public interface Observer {
    void update(Agendamento agendamento);
}
