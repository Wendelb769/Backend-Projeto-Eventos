package com.batista.gerenciador_de_eventos.service;

import com.batista.gerenciador_de_eventos.config.JWTUserData;
import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import com.batista.gerenciador_de_eventos.repository.EventoRepository;
import com.batista.gerenciador_de_eventos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
        boolean admin = usuarioLogado.role().equals("ADMIN");

        if (donoDaConta || admin){
            eventoRepository.deleteEventosUsuarioById(id);
            usuarioRepository.deleteById(id);
        } else{
            throw new AccessDeniedException("Você não tem permissão para excluir esta conta");
        }
    }

    public void atualizarUsuarioPorId(Long id, Usuario usuario){
        Usuario usuarioEntity = usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Id não encontrado"));

        Usuario usuarioAtualizado = Usuario.builder()
                .id(usuarioEntity.getId())
                .nome(usuario.getNome() != null ? usuario.getNome() : usuarioEntity.getNome())
                .email(usuario.getEmail() != null ? usuario.getEmail() : usuarioEntity.getEmail())
                .senha(usuario.getSenha() != null ? usuario.getSenha() : usuarioEntity.getSenha())
                .build();

        usuarioRepository.saveAndFlush(usuarioAtualizado);
    }

}
