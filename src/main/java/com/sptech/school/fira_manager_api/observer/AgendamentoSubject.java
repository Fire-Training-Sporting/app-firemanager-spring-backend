package com.sptech.school.fira_manager_api.observer;

import com.sptech.school.fira_manager_api.model.Agendamento;

import java.util.ArrayList;
import java.util.List;

public class AgendamentoSubject {

    // Aqui é uma lista que pode ser adicionado qualquer instancia
    // que implemente a interface Observer
    private final List<Observer> observers = new ArrayList<>();

    // Aqui é o metodo para adicionar na lista
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Aqui é o metodo para remover da lista
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Aqui é o metodo para limpar a lista
    public void clearObservers() {
        observers.clear();
    }

    // Este é um metodo de notificar todas as instancias na lista,
    // porém só sera notificado o professor e o aluno envolvido
    // devido dentro da classe AlunoObserver e ProfessorObserver
    // o metodo update() ter um if para notificar somente se o
    // id do agendamento for exatamente igual ao id da instancia.
    public void notifyObservers(Agendamento agendamento) {
        for (Observer observer : observers) {
            observer.update(agendamento);
        }
    }
}

