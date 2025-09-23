package org.example.emailnotification.consumer;

import org.example.emailnotification.dto.request.EmailMessage;
import org.example.emailnotification.dto.request.EmailMimeRequestDto;
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
        if (email instanceof EmailSimpleRequestDto) {
            emailService.sendSimpleEmail((EmailSimpleRequestDto) email);
        } else if (email instanceof EmailMimeRequestDto) {
            emailService.sendMimeEmail((EmailMimeRequestDto) email);
        }
    }

}
