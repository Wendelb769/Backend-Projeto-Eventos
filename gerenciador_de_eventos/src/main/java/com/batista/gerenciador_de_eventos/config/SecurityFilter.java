package com.batista.gerenciador_de_eventos.config;

import com.batista.gerenciador_de_eventos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenConfig tokenConfig;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizedHeader = request.getHeader("Authorization");

        if (Strings.isNotEmpty(authorizedHeader) && authorizedHeader.startsWith("Bearer ")){

            String token = authorizedHeader.substring(7);
            Optional<JWTUserData> optUser = tokenConfig.validateToken(token);

            if (optUser.isPresent()){
                JWTUserData userData = optUser.get();
                var usuarioNoBanco = usuarioRepository.findById(userData.usuarioId()).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userData, null, usuarioNoBanco.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

}

