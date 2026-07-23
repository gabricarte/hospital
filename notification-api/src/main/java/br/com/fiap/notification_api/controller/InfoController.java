package br.com.fiap.notification_api.controller;

import br.com.fiap.notification_api.domain.dto.InfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class InfoController {
    private final BuildProperties buildProperties;

    @GetMapping("/info")
    public ResponseEntity<InfoDTO> find() {
        return ResponseEntity
                .ok()
                .body(InfoDTO.builder()
                        .version(this.buildProperties.getVersion())
                        .build());
    }
}

