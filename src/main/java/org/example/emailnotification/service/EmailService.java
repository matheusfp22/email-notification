package org.example.emailnotification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.emailnotification.dto.request.EmailMimeRequestDto;
import org.example.emailnotification.dto.request.EmailRequestDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(EmailRequestDto request) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("noreply@mail.com");
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getMessage());

        mailSender.send(message);
    }

    public void sendMimeEmail(EmailMimeRequestDto request) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("noreply@mail.com");
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());

        String html = """
                <h4>Olá, %s!</h2>
                <p>%s</p>
                <img src='cid:img'>
                """.formatted(request.getTo(), request.getMessage());

        if (request.getImage() != null && !request.getImage().isEmpty())
            helper.addInline("img", request.getImage(), Objects.requireNonNull(request.getImage().getContentType()));
        helper.setText(html, true);

        for (MultipartFile file : request.getAttachments()) {
            if (!file.isEmpty()) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }

        mailSender.send(mimeMessage);
    }

}
