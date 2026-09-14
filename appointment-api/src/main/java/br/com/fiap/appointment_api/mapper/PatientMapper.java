package br.com.fiap.appointment_api.mapper;

import br.com.fiap.appointment_api.domain.dto.response.PatientResponse;
import br.com.fiap.appointment_api.domain.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientResponse toResponse(Patient patient) {

        return new PatientResponse(
                patient.getId(),
                patient.getName(),
                patient.getCpf(),
                patient.getEmail(),
                patient.getUser().getUsername()
        );
    }
}