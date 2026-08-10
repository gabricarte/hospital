package br.com.fiap.appointment_api.domain.dto.response;

public record PatientResponse(
        Long id,
        String name,
        String cpf,
        String email
) {
}