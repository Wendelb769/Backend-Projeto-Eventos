package com.batista.gerenciador_de_eventos.repository;

import com.batista.gerenciador_de_eventos.entity.Evento.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}