package com.batista.gerenciador_de_eventos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistrarUsuarioRequest(@NotBlank(message = "O nome não pode estar vazio!") String nome,

                                      @Email(message = "Formato de email inválido!") @NotBlank(message = "O email não pode estar vazio!") String email,

                                      @NotBlank(message = "A senha não pode estar vazia!")
                                      @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                                              message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial") String senha) {
}
