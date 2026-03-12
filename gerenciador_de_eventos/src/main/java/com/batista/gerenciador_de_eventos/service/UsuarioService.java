package com.batista.gerenciador_de_eventos.service;

import com.batista.gerenciador_de_eventos.config.JWTUserData;
import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import com.batista.gerenciador_de_eventos.entity.Usuario.UsuarioRole;
import com.batista.gerenciador_de_eventos.repository.EventoRepository;
import com.batista.gerenciador_de_eventos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    public final UsuarioRepository usuarioRepository;
    public final EventoRepository eventoRepository;

    public Usuario buscarUsuarioPorId(Long id){
       return usuarioRepository.findById(id).orElseThrow(
               () -> new RuntimeException("Id não encontrado")
       );
    }

    @Transactional
    public void deletarUsuarioPorId(Long id, JWTUserData usuarioLogado){
        Usuario usuarioNoBanco = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id não encontrado"));

        boolean donoDaConta = usuarioLogado.usuarioId().equals(usuarioNoBanco.getId());

        if (donoDaConta || isAdmin()){
            eventoRepository.deleteAllEventosUsuarioById(id);
            usuarioRepository.deleteById(id);
        } else{
            throw new AccessDeniedException("Você não tem permissão para excluir esta conta.");
        }
    }

    @Transactional
    public void atualizarUsuarioPorId(Long id, JWTUserData usuarioLogado, Usuario usuario){
        Usuario usuarioEntity = usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Id não encontrado"));

        boolean donoDaConta = usuarioLogado.usuarioId().equals(usuarioEntity.getId());

        if (donoDaConta || isAdmin()){
            Usuario usuarioAtualizado = Usuario.builder()
                    .id(usuarioEntity.getId())
                    .nome(usuario.getNome() != null ? usuario.getNome() : usuarioEntity.getNome())
                    .email(usuario.getEmail() != null ? usuario.getEmail() : usuarioEntity.getEmail())
                    .senha(usuario.getSenha() != null ? usuario.getSenha() : usuarioEntity.getSenha())
                    .role(isAdmin() && usuario.getRole() != null ? usuario.getRole() : usuarioEntity.getRole())
                    .build();

            usuarioRepository.saveAndFlush(usuarioAtualizado);
        } else{
            throw new AccessDeniedException("Você não tem permissão pra atualizar essa conta.");
        }

    }

    private boolean isAdmin(){
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

}
