package br.com.fiap.notification_api.domain.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InfoDTO {
    private String version;
}
