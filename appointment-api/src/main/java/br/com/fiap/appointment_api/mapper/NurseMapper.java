package br.com.fiap.appointment_api.mapper;

import br.com.fiap.appointment_api.domain.dto.response.NurseResponse;
import br.com.fiap.appointment_api.domain.entity.Nurse;
import org.springframework.stereotype.Component;

@Component
public class NurseMapper {

    public NurseResponse toResponse(Nurse nurse) {
        return new NurseResponse(
                nurse.getId(),
                nurse.getName(),
                nurse.getCoren(),
                nurse.getUser() != null ? nurse.getUser().getUsername() : null
        );
    }
}