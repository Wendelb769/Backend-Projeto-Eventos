package com.batista.gerenciador_de_eventos.dto.response;

import jakarta.validation.constraints.NotEmpty;

public record LoginResponse(String token) {
}
