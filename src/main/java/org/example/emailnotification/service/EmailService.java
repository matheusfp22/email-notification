package org.example.emailnotification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.emailnotification.dto.request.EmailRequestDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(EmailRequestDto request)  {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(request.getFrom());
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getMessage());

        mailSender.send(message);
    }

    public void sendMimeEmail(EmailRequestDto request, File file) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(request.getFrom());
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setText(request.getMessage(), true);
        helper.addAttachment(file.getName(), file);

        mailSender.send(mimeMessage);
    }

}
