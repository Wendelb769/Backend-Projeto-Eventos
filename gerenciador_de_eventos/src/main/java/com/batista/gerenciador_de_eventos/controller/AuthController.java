package com.batista.gerenciador_de_eventos.controller;

import com.batista.gerenciador_de_eventos.config.TokenConfig;
import com.batista.gerenciador_de_eventos.dto.request.LoginRequest;
import com.batista.gerenciador_de_eventos.dto.request.RegistrarUsuarioRequest;
import com.batista.gerenciador_de_eventos.dto.response.LoginResponse;
import com.batista.gerenciador_de_eventos.dto.response.RegistrarUsuarioResponse;
import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import com.batista.gerenciador_de_eventos.entity.Usuario.UsuarioRole;
import com.batista.gerenciador_de_eventos.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        String token = tokenConfig.gerarToken(usuarioLogado);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<RegistrarUsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request){
        Usuario novoUsuario = new Usuario();

        novoUsuario.setNome(request.nome());
        novoUsuario.setEmail(request.email());
        novoUsuario.setRole(UsuarioRole.USUARIO);
        novoUsuario.setSenha(passwordEncoder.encode(request.senha()));

        usuarioRepository.saveAndFlush(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrarUsuarioResponse(novoUsuario.getNome(), novoUsuario.getEmail()));
    }

}
