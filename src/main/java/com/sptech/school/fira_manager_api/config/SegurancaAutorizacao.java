package com.sptech.school.fira_manager_api.config;

import com.sptech.school.fira_manager_api.model.Agendamento;
import com.sptech.school.fira_manager_api.model.Usuario;
import com.sptech.school.fira_manager_api.repository.AgendamentoRepository;
import com.sptech.school.fira_manager_api.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("segurancaAutorizacao")
public class SegurancaAutorizacao {

    private final UsuarioRepository usuarioRepository;
    private final AgendamentoRepository agendamentoRepository;

    public SegurancaAutorizacao(UsuarioRepository usuarioRepository,
                                AgendamentoRepository agendamentoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.agendamentoRepository = agendamentoRepository;
    }

    public boolean usuarioAtualEh(Long usuarioId) {
        return obterUsuarioAtual()
                .map(usuario -> usuario.getId().equals(usuarioId))
                .orElse(false);
    }

    public boolean usuarioEhAluno(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getTipoUsuario().getCargo().equalsIgnoreCase("aluno"))
                .orElse(false);
    }

    public boolean agendamentoEhDoUsuarioAtual(Long agendamentoId) {
        Usuario usuarioAtual = obterUsuarioAtual().orElse(null);
        if (usuarioAtual == null) {
            return false;
        }

        Agendamento agendamento = agendamentoRepository.findById(agendamentoId).orElse(null);
        if (agendamento == null) {
            return false;
        }

        boolean aluno = temRole("ROLE_ALUNO")
                && (temId(agendamento.getAluno(), usuarioAtual.getId())
                    || agendamento.getAlunos() != null && agendamento.getAlunos().stream()
                            .anyMatch(alunoGrupo -> temId(alunoGrupo, usuarioAtual.getId())));
        boolean professor = temRole("ROLE_PROFESSOR")
                && (temId(agendamento.getProfessor(), usuarioAtual.getId())
                    || temId(agendamento.getAuxiliar(), usuarioAtual.getId())
                    || temId(agendamento.getRebatedor(), usuarioAtual.getId()));

        return aluno || professor;
    }

    public Long usuarioAtualId() {
        return obterUsuarioAtual()
                .map(Usuario::getId)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
    }

    public boolean usuarioAtualTemRole(String role) {
        return temRole(role);
    }

    private java.util.Optional<Usuario> obterUsuarioAtual() {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null || !autenticacao.isAuthenticated()) {
            return java.util.Optional.empty();
        }
        return usuarioRepository.findByEmail(autenticacao.getName());
    }

    private boolean temRole(String role) {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        return autenticacao != null && autenticacao.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    private boolean temId(Usuario usuario, Long id) {
        return usuario != null && usuario.getId().equals(id);
    }
}
