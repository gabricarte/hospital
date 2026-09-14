package br.com.fiap.appointment_api.controller;

import br.com.fiap.appointment_api.domain.dto.CreateNurseInput;
import br.com.fiap.appointment_api.domain.dto.response.NurseResponse;
import br.com.fiap.appointment_api.mapper.NurseMapper;
import br.com.fiap.appointment_api.service.NurseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NurseGraphQLController {

    private final NurseService nurseService;
    private final NurseMapper nurseMapper;

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public NurseResponse createNurse(
            @Argument @Valid CreateNurseInput input
    ) {
        return nurseMapper.toResponse(
                nurseService.createNurse(
                        input.name(),
                        input.coren(),
                        input.username(),
                        input.password()
                )
        );
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public NurseResponse nurse(
            @Argument Long nurseId
    ) {
        return nurseMapper.toResponse(
                nurseService.findById(nurseId)
        );
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public List<NurseResponse> nurses() {
        return nurseService.findAll()
                .stream()
                .map(nurseMapper::toResponse)
                .toList();
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public NurseResponse updateNurse(
            @Argument Long nurseId,
            @Argument String name
    ) {
        return nurseMapper.toResponse(
                nurseService.updateNurse(
                        nurseId,
                        name
                )
        );
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteNurse(
            @Argument Long nurseId
    ) {
        nurseService.deleteNurse(nurseId);
        return true;
    }
}
