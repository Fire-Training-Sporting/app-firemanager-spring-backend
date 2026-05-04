package com.sptech.school.fira_manager_api.observer;


// Essa interface faz com que todos os observer quem receberá atualizações)
// sejam obrigados a implementar e sobreescrever o metodo update.

import com.sptech.school.fira_manager_api.model.Agendamento;

public interface Observer {
    void update(Agendamento agendamento);
}
