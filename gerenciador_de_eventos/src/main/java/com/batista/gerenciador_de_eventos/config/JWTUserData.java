package com.batista.gerenciador_de_eventos.config;

import com.batista.gerenciador_de_eventos.entity.Usuario.UsuarioRole;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Builder
public record JWTUserData(Long usuarioId, String email, String role) {

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("ADMIN".equals(this.role)){
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USUARIO"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USUARIO"));
        }
    }
}
