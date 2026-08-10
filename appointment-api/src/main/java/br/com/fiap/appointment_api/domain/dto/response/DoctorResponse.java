package br.com.fiap.appointment_api.domain.dto.response;

public record DoctorResponse(

        Long id,
        String name,
        String crm,
        String specialty
) {
}