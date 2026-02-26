package com.batista.gerenciador_de_eventos.repository;

import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> id(Long id);

    UserDetails findUserByEmail(String username);

}
