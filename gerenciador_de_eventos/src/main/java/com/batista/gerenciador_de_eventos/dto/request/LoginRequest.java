package com.batista.gerenciador_de_eventos.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty(message = "E-mail não pode estar vazio") String email,
                          @NotEmpty(message = "Senha não pode estar vazia") String senha) {

}
