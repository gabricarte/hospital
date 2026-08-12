package br.com.fiap.appointment_api.controller;

import br.com.fiap.appointment_api.domain.dto.CreatePatientInput;
import br.com.fiap.appointment_api.domain.dto.response.PatientResponse;
import br.com.fiap.appointment_api.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PatientGraphQLController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @QueryMapping
    public PatientResponse patient(
            @Argument Long patientId
    ) {
        return patientMapper.toResponse(
                patientService.findById(patientId)
        );
    }

    @QueryMapping
    public List<PatientResponse> patients() {
        return patientService.findAll()
                .stream()
                .map(patientMapper::toResponse)
                .toList();
    }

    @MutationMapping
    public PatientResponse createPatient(
            @Argument @Valid CreatePatientInput input
    ) {
        return patientMapper.toResponse(
                patientService.createPatient(
                        input.name(),
                        input.cpf(),
                        input.email()
                )
        );
    }

    @MutationMapping
    public PatientResponse updatePatient(
            @Argument Long patientId,
            @Argument String name,
            @Argument String email
    ) {
        return patientMapper.toResponse(
                patientService.updatePatient(
                        patientId,
                        name,
                        email
                )
        );
    }

    @MutationMapping
    public Boolean deletePatient(
            @Argument Long patientId
    ) {
        patientService.deletePatient(patientId);
        return true;
    }
}
