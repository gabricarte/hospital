package br.com.fiap.appointment_api.controller;

import br.com.fiap.appointment_api.domain.dto.CreateDoctorInput;
import br.com.fiap.appointment_api.domain.dto.response.DoctorResponse;
import br.com.fiap.appointment_api.mapper.DoctorMapper;
import br.com.fiap.appointment_api.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DoctorGraphQLController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorResponse createDoctor(
            @Argument CreateDoctorInput input
    ) {
        return doctorMapper.toResponse(
                doctorService.createDoctor(
                        input.name(),
                        input.crm(),
                        input.specialty(),
                        input.username(),
                        input.password()
                )
        );
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public DoctorResponse doctor(
            @Argument Long doctorId
    ) {
        return doctorMapper.toResponse(
                doctorService.findById(doctorId)
        );
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DoctorResponse> doctors() {
        return doctorService.findAll()
                .stream()
                .map(doctorMapper::toResponse)
                .toList();
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorResponse updateDoctor(
            @Argument Long doctorId,
            @Argument String name,
            @Argument String specialty
    ) {
        return doctorMapper.toResponse(
                doctorService.updateDoctor(
                        doctorId,
                        name,
                        specialty
                )
        );
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteDoctor(
            @Argument Long doctorId
    ) {
        doctorService.deleteDoctor(doctorId);
        return true;
    }
}