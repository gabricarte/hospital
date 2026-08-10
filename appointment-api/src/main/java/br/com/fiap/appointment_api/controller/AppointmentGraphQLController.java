package br.com.fiap.appointment_api.controller;

import br.com.fiap.appointment_api.domain.dto.response.AppointmentResponse;
import br.com.fiap.appointment_api.mapper.AppointmentMapper;
import br.com.fiap.appointment_api.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppointmentGraphQLController {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @QueryMapping
    public List<AppointmentResponse> appointmentsByPatient(
            @Argument Long patientId
    ) {
        return appointmentRepository
                .findByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @QueryMapping
    public List<AppointmentResponse> futureAppointmentsByPatient(
            @Argument Long patientId
    ) {
        return appointmentRepository
                .findByPatientIdAndDateTimeAfter(
                        patientId,
                        LocalDateTime.now()
                )
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @QueryMapping
    public List<AppointmentResponse> appointmentHistory(
            @Argument Long patientId
    ) {
        return appointmentRepository
                .findByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }
}