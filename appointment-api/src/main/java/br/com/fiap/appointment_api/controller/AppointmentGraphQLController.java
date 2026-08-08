package br.com.fiap.appointment_api.controller;

import br.com.fiap.appointment_api.domain.entity.Appointment;
import br.com.fiap.appointment_api.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppointmentGraphQLController {

    private final AppointmentService appointmentService;

    @QueryMapping
    public List<Appointment> appointmentsByPatient(
            @Argument Long patientId
    ) {
        return appointmentService.findByPatientId(patientId);
    }

    @QueryMapping
    public List<Appointment> futureAppointmentsByPatient(
            @Argument Long patientId
    ) {
        return appointmentService.findFutureAppointmentsByPatient(
                patientId,
                LocalDateTime.now()
        );
    }

    @QueryMapping
    public List<Appointment> appointmentHistory(
            @Argument Long patientId
    ) {
        return appointmentService.findAppointmentHistory(patientId);
    }
}