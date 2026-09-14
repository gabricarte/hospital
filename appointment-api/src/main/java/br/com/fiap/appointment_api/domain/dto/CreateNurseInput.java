package br.com.fiap.appointment_api.domain.dto;

public record CreateNurseInput(
        String name,
        String coren,
        String username,
        String password
) {}