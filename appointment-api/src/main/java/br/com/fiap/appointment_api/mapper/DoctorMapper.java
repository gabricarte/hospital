package br.com.fiap.appointment_api.mapper;

import br.com.fiap.appointment_api.domain.dto.response.DoctorResponse;
import br.com.fiap.appointment_api.domain.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getCrm(),
                doctor.getSpecialty(),
                doctor.getUser().getUsername()
        );
    }
}