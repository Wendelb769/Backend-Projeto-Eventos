package com.batista.gerenciador_de_eventos.controller;

import com.batista.gerenciador_de_eventos.config.JWTUserData;
import com.batista.gerenciador_de_eventos.dto.request.EventoRequest;
import com.batista.gerenciador_de_eventos.dto.response.EventoResponse;
import com.batista.gerenciador_de_eventos.entity.Evento.Evento;
import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import com.batista.gerenciador_de_eventos.repository.UsuarioRepository;
import com.batista.gerenciador_de_eventos.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/evento")
public class EventoController {
    private final EventoService eventoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Evento> buscarEvento(@PathVariable Long id){
        return ResponseEntity.ok(eventoService.buscarEventoPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Evento>> buscarTodosEventos(){
        return ResponseEntity.ok(eventoService.buscarTodosOsEventos());
    }

    @PostMapping
    public ResponseEntity<Evento> salvarEvento(@Valid @RequestBody EventoRequest request){
        JWTUserData jwtUserData = (JWTUserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuarioLogado = usuarioRepository.findById(jwtUserData.usuarioId()).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        Evento novoEvento = eventoService.salvarEvento(request, usuarioLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> atualizarEvento(@PathVariable Long id, @RequestBody Evento evento){
        eventoService.atualizarEvento(id, evento);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{idEvento}")
    public ResponseEntity<Void> deletarEvento(@PathVariable Long idEvento, @AuthenticationPrincipal JWTUserData usuarioLogado){
        eventoService.deleteEventoPorId(idEvento, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

}
