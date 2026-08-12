package br.com.fiap.appointment_api.domain.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentInput (

        @NotNull(message = "O paciente é obrigatório.")
        Long patientId,

        @NotNull(message = "O médico é obrigatório.")
        Long doctorId,

        @NotNull(message = "A data e hora da consulta são obrigatórias.")
        @Future(message = "A consulta deve ser agendada para uma data futura.")
        LocalDateTime dateTime
) {
}