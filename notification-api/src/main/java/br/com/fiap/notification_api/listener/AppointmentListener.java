package br.com.fiap.notification_api.listener;

import br.com.fiap.notification_api.domain.dto.AppointmentNotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentListener {

    private final tools.jackson.databind.ObjectMapper objectMapper;

    @RabbitListener(queues = "appointment.created.queue")
    public void receiveMessage(String messageJson) {
        try {
            AppointmentNotificationDTO dto = objectMapper.readValue(messageJson, AppointmentNotificationDTO.class);

            System.out.println("====== NOVA NOTIFICAÇÃO RECEBIDA ======");
            System.out.println("Paciente: " + dto.patientName());
            System.out.println("E-mail: " + dto.patientEmail());
            System.out.println("Médico: " + dto.doctorName());
            System.out.println("Data: " + dto.appointmentDateTime());
            System.out.println("=======================================");

            // aqui entrará a lógica de envio de e-mail (JavaMailSender)

        } catch (Exception e) {
            System.err.println("Falha ao converter a mensagem da fila: " + e.getMessage());
        }
    }
}
