package br.com.fiap.appointment_api.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDoctorInput(

        @NotBlank(message = "O nome do médico é obrigatório.")
        @Size(max = 100, message = "O nome deve possuir no máximo 100 caracteres.")
        String name,

        @NotBlank(message = "O CRM é obrigatório.")
        @Size(max = 20, message = "O CRM deve possuir no máximo 20 caracteres.")
        String crm,

        @NotBlank(message = "A especialidade é obrigatória.")
        @Size(max = 50, message = "A especialidade deve possuir no máximo 50 caracteres.")
        String specialty,

        @NotBlank(message = "O username é obrigatório.")
        @Size(max = 50, message = "O e-mail deve possuir no máximo 50 caracteres.")
        String username,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(max = 50, message = "A senha deve possuir no máximo 50 caracteres.")
        String password
) {
}