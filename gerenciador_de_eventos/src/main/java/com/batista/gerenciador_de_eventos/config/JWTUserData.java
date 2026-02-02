package com.batista.gerenciador_de_eventos.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long usuarioId, String email) {

}
