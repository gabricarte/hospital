package br.com.fiap.notification_api.listener;

import br.com.fiap.notification_api.domain.dto.AppointmentNotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import java.time.format.DateTimeFormatter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentListener {

    private final tools.jackson.databind.ObjectMapper objectMapper;
    private final JavaMailSender mailSender;

    @RabbitListener(queues = "appointment.created.queue")
    public void receiveMessage(String messageJson) {
        try {
            AppointmentNotificationDTO dto = objectMapper.readValue(messageJson, AppointmentNotificationDTO.class);

            System.out.println("Enviando e-mail...");
            sendMail(dto);
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());

            throw new AmqpRejectAndDontRequeueException("Falha no envio do e-mail", e);
        }
    }

    private void sendMail(AppointmentNotificationDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String date = dto.appointmentDateTime().format(formatter);

        message.setTo(dto.patientEmail());
        message.setSubject("Consulta marcada com sucesso. ");

        message.setText(dto.patientName() + ", sua consulta foi marcada com sucesso. Com " + dto.doctorName() + " em: "
        + date);

        message.setFrom("guitarrasgabi@gmail.com");

        mailSender.send(message);
    }
}
