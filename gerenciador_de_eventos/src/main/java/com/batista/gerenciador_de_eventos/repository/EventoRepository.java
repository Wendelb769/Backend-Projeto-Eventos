package com.batista.gerenciador_de_eventos.repository;

import com.batista.gerenciador_de_eventos.entity.Evento.Evento;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Evento e WHERE e.usuarioId.id = :id")
    void deleteAllEventosUsuarioById(Long id);
}