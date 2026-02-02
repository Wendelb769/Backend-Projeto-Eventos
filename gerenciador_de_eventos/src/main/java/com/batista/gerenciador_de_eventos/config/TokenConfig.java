package com.batista.gerenciador_de_eventos.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    private String secret = "secret";
    Algorithm algoritmo = Algorithm.HMAC256(secret);

    public String gerarToken(Usuario usuario){
        return JWT.create()
                .withClaim("UsuarioId", usuario.getId())
                .withSubject(usuario.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algoritmo);
    }

    public Optional<JWTUserData> validateToken(String token){
        try{
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algoritmo)
                    .build().verify(token);

            return Optional.of(JWTUserData.builder()
                    .usuarioId(decodedJWT.getClaim("UsuarioId").asLong())
                    .email(decodedJWT.getSubject())
                    .build());

        }
        catch (JWTVerificationException ex){
            return Optional.empty();
        }
    }

}
