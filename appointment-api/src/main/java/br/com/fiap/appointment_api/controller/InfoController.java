package br.com.fiap.appointment_api.controller;

import br.com.fiap.appointment_api.domain.dto.AppointmentNotificationDTO;
import br.com.fiap.appointment_api.domain.dto.InfoDTO;
import br.com.fiap.appointment_api.publisher.AppointmentMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class InfoController {
    private final BuildProperties buildProperties;

    private final AppointmentMessagePublisher messagePublisher;

    private final ObjectMapper objectMapper;

    @GetMapping("/info")
    public ResponseEntity<InfoDTO> find() {

        AppointmentNotificationDTO dto = new AppointmentNotificationDTO(
                "gabicode0@gmail.com",
                "Gabriela Ricarte",
                "Dra. Ana",
                LocalDateTime.now()
        );

        String mensagemJson = objectMapper.writeValueAsString(dto);

        System.out.println("Enviando mensagem....");
        messagePublisher.sendAppointmentMessage(mensagemJson);

        return ResponseEntity
                .ok()
                .body(InfoDTO.builder()
                        .version(this.buildProperties.getVersion())
                        .build());
    }
}

