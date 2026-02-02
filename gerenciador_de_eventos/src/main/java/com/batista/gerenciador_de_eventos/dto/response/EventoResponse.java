package com.batista.gerenciador_de_eventos.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventoResponse(String titulo, LocalTime horario, LocalDate data, String descricao, String imagem, String local) {
}
