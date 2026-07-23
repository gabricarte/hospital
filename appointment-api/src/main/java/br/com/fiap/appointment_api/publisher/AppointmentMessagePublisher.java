package br.com.fiap.appointment_api.publisher;

import br.com.fiap.appointment_api.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendAppointmentMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.APPOINTMENT_CREATED_QUEUE, message);
    }
}