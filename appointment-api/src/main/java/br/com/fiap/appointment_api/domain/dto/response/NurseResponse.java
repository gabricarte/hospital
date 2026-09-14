package br.com.fiap.appointment_api.domain.dto.response;

public record NurseResponse(
        Long id,
        String name,
        String coren,
        String username
) {}