package com.batista.gerenciador_de_eventos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventoRequest(@NotBlank(message = "O título não pode estar vazio!")String titulo,
                            @NotNull(message = "O horario não pode estar vazio!") LocalTime horario,
                            @NotNull(message = "A data não pode estar vazio!") LocalDate data,
                            @NotBlank(message = "A descrição não pode estar vazia!") String descricao,
                            String imagem,
                            @NotBlank(message = "O local não pode estar vazio!") String local) {
}
