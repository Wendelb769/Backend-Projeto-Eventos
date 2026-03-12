package com.batista.gerenciador_de_eventos.service;

import com.batista.gerenciador_de_eventos.config.JWTUserData;
import com.batista.gerenciador_de_eventos.dto.request.EventoRequest;
import com.batista.gerenciador_de_eventos.entity.Evento.Evento;
import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import com.batista.gerenciador_de_eventos.repository.EventoRepository;
import com.batista.gerenciador_de_eventos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventoService {
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public void salvarEvento(EventoRequest request, JWTUserData usuarioLogado) {

        Usuario dono = usuarioRepository.findById(usuarioLogado.usuarioId()).orElseThrow(() -> new EntityNotFoundException("Id não encontrado"));

        Evento novoEvento = Evento.builder()
                .titulo(request.titulo())
                .horario(request.horario())
                .data(request.data())
                .descricao(request.descricao())
                .imagem(request.imagem())
                .local(request.local())
                .usuarioId(dono)
                .build();

        eventoRepository.saveAndFlush(novoEvento);

    }

    public List<Evento> buscarTodosOsEventos() {
        return eventoRepository.findAll();
    }

    public Evento buscarEventoPorId(Long id) {
        return eventoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Id não encontrado!"));
    }

    @Transactional
    public void deleteEventoPorId(Long idEvento, JWTUserData usuarioLogado) {

        Evento evento = eventoRepository.findById(idEvento).orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        Long idDonoDoEvento = evento.getUsuarioId().getId();
        boolean donoDoEvento = usuarioLogado.usuarioId().equals(idDonoDoEvento);

        if (donoDoEvento || isAdmin()){
            eventoRepository.deleteById(idEvento);
        } else{
            throw new AccessDeniedException("Sem permissão para excluir o evento desse usuario");
        }
    }

    public void atualizarEvento(Long id, Evento evento) {
        Evento eventoEntity = eventoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Id não encontrado!")
        );

        Evento eventoAtualizado = Evento.builder()
                .id(id)
                .usuarioId(evento.getUsuarioId())
                .titulo(evento.getTitulo() != null ? evento.getTitulo() : eventoEntity.getTitulo())
                .horario(evento.getHorario() != null ? evento.getHorario() : eventoEntity.getHorario())
                .data(evento.getData() != null ? evento.getData() : eventoEntity.getData())
                .descricao(evento.getDescricao() != null ? evento.getDescricao() : eventoEntity.getDescricao())
                .imagem(evento.getImagem() != null ? evento.getImagem() : eventoEntity.getImagem())
                .local(evento.getLocal() != null ? evento.getLocal() : eventoEntity.getLocal())
                .build();

        eventoRepository.saveAndFlush(eventoAtualizado);
    }

    private boolean isAdmin(){
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

}