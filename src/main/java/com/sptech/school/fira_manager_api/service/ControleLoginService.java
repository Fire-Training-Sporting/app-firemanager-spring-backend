package com.sptech.school.fira_manager_api.service;

import java.time.LocalDateTime;

public class ControleLoginService {

    private int tentativas = 0;
    private LocalDateTime tempoBloqueado;

    public void incrementar() {
        tentativas++;
    }

    public int getTentativas() {
        return tentativas;
    }

    public void resetar() {
        tentativas = 0;
        tempoBloqueado = null;
    }

    public void bloquear(LocalDateTime tempo) {
        tempoBloqueado = tempo;
        tentativas = 0;
    }

    public boolean estaBloqueado() {
        return tempoBloqueado != null && tempoBloqueado.isAfter(LocalDateTime.now());
    }
}