package org.example.emailnotification.consumer;

import org.example.emailnotification.dto.request.EmailMessage;
import org.example.emailnotification.dto.request.EmailMimeQueueRequestDto;
import org.example.emailnotification.dto.request.EmailSimpleRequestDto;
import org.example.emailnotification.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "emails-queue")
    public void receiveEmail(EmailMessage email) {
        try {
            if (email instanceof EmailSimpleRequestDto simpleEmail) {
                emailService.sendSimpleEmail(simpleEmail);
            } else if (email instanceof EmailMimeQueueRequestDto mimeEmail) {
                emailService.sendMimeEmail(mimeEmail);
            } else {
                System.err.println("Mensagem recebida com tipo desconhecido: " + email.getClass());
            }
        } catch (Exception e) {
            System.err.println("Error processing email from queue: " + e.getMessage());
        }
    }

}
