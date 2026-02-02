package com.batista.gerenciador_de_eventos.entity.Evento;

import com.batista.gerenciador_de_eventos.entity.Usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "evento")
@Entity
public class Evento {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioId;

    @NotBlank(message = "O título não pode estar vazio!")
    @Column(name = "titulo", nullable = false, length = 50)
    private String titulo;

    @NotNull(message = "O horario não pode estar vazio!")
    @Column(name = "horario", nullable = false)
    private LocalTime horario;

    @NotNull(message = "A data não pode estar vazio!")
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @NotBlank(message = "A descrição não pode estar vazia!")
    @Column(name = "descricao", nullable = false, length = 240)
    private String descricao;

    @Column(name = "imagem", nullable = false)
    private String imagem;

    @NotBlank(message = "O local não pode estar vazio!")
    @Column(name = "local", nullable = false, length = 110)
    private String local;

}
