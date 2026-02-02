package com.batista.gerenciador_de_eventos.service;

import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import com.batista.gerenciador_de_eventos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    public final UsuarioRepository usuarioRepository;

    // FALTA A CLASSE DE CADASTRO DE USUARIOS!!!

    public Usuario salvarUsuario(Usuario usuario){
        return usuarioRepository.saveAndFlush(usuario);
    }

    public Usuario buscarUsuarioPorId(Long id){
       return usuarioRepository.findById(id).orElseThrow(
               () -> new RuntimeException("Id não encontrado")
       );
    }

    public void deletarUsuarioPorId(Long id){
        usuarioRepository.deleteById(id);

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
