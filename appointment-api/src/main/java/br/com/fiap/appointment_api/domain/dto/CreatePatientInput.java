package br.com.fiap.appointment_api.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePatientInput(

        @NotBlank(message = "O nome do paciente é obrigatório.")
        @Size(max = 100, message = "O nome deve possuir no máximo 100 caracteres.")
        String name,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(
                regexp = "\\d{11}",
                message = "O CPF deve possuir exatamente 11 dígitos."
        )
        String cpf,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail informado é inválido.")
        @Size(max = 255, message = "O e-mail deve possuir no máximo 255 caracteres.")
        String email,

        @NotBlank(message = "O username é obrigatório.")
        @Size(max = 50, message = "O e-mail deve possuir no máximo 50 caracteres.")
        String username,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(max = 50, message = "A senha deve possuir no máximo 50 caracteres.")
        String password
) {
}