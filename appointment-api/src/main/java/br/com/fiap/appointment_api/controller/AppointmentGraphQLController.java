package br.com.fiap.appointment_api.controller;

import br.com.fiap.appointment_api.domain.dto.response.AppointmentResponse;
import br.com.fiap.appointment_api.mapper.AppointmentMapper;
import br.com.fiap.appointment_api.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppointmentGraphQLController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @MutationMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN')")
    public AppointmentResponse createAppointment(
            @Argument Long patientId,
            @Argument Long doctorId,
            @Argument String dateTime
    ) {
        LocalDateTime parsedDate = LocalDateTime.parse(dateTime);

        return appointmentMapper.toResponse(
                appointmentService.create(patientId, doctorId, parsedDate)
        );
    }

    @QueryMapping
    @PreAuthorize("hasRole('PATIENT')")
    public List<AppointmentResponse> patientAppointments(
            Authentication authentication
    ) {
        return appointmentService
                .findByUsername(authentication.getName())
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN')")
    public List<AppointmentResponse> appointmentHistory(
            @Argument Long patientId
    ) {
        return appointmentService.findHistoryByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN')")
    public AppointmentResponse updateAppointment(
            @Argument Long id,
            @Argument Long doctorId,
            @Argument String dateTime
    ) {
        return appointmentMapper.toResponse(
                appointmentService.update(
                        id,
                        doctorId,
                        LocalDateTime.parse(dateTime)
                )
        );
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN')")
    public AppointmentResponse cancelAppointment(@Argument Long id) {
        return appointmentMapper.toResponse(
                appointmentService.cancel(id)
        );
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN')")
    public AppointmentResponse completeAppointment(@Argument Long id) {
        return appointmentMapper.toResponse(
                appointmentService.complete(id)
        );
    }
}