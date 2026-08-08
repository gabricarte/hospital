package br.com.fiap.appointment_api.controller;

import br.com.fiap.appointment_api.domain.entity.Appointment;
import br.com.fiap.appointment_api.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppointmentGraphQLController {

    private final AppointmentRepository appointmentRepository;

    @QueryMapping
    public List<Appointment> appointmentsByPatient(
            @Argument Long patientId
    ) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @QueryMapping
    public List<Appointment> futureAppointmentsByPatient(
            @Argument Long patientId
    ) {
        return appointmentRepository.findByPatientIdAndDateTimeAfter(
                patientId,
                LocalDateTime.now()
        );
    }

    @QueryMapping
    public List<Appointment> appointmentHistory(
            @Argument Long patientId
    ) {
        return appointmentRepository.findByPatientId(patientId);
    }
}