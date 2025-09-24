package org.example.emailnotification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.emailnotification.dto.request.EmailMimeQueueRequestDto;
import org.example.emailnotification.dto.request.EmailSimpleRequestDto;
import org.example.emailnotification.entity.EmailHistoryEntity;
import org.example.emailnotification.repository.EmailHistoryRepository;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailHistoryRepository emailHistoryRepository;
    private final StorageService storageService;

    public EmailService(JavaMailSender mailSender, EmailHistoryRepository emailHistoryRepository, StorageService storageService) {
        this.mailSender = mailSender;
        this.emailHistoryRepository = emailHistoryRepository;
        this.storageService = storageService;
    }

    public void sendSimpleEmail(EmailSimpleRequestDto request) {
        EmailHistoryEntity email = EmailHistoryEntity.builder()
                .receiver(request.getTo())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("noreply@mail.com");
            message.setTo(email.getReceiver());
            message.setSubject(email.getSubject());
            message.setText(email.getMessage());

            mailSender.send(message);
        } catch (MailException m) {
            email.setStatus("FAILED");
            email.setError(m.getMessage());
        } finally {
            emailHistoryRepository.save(email);
        }

    }

    public void sendMimeEmail(EmailMimeQueueRequestDto request) {
        EmailHistoryEntity email = EmailHistoryEntity.builder()
                .receiver(request.getTo())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status("SENT")
                .imagePath(request.getImagePath())
                .attachments(request.getAttachments())
                .createdAt(LocalDateTime.now())
                .build();

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("noreply@mail.com");
            helper.setTo(email.getReceiver());
            helper.setSubject(email.getSubject());

            StringBuilder html = new StringBuilder();
            html.append("<h4>Olá, ").append(email.getReceiver()).append("!</h4>");
            html.append("<p>").append(email.getMessage()).append("</p>");

            if (email.getImagePath() != null) {
                Resource image = storageService.load(email.getImagePath());
                helper.addInline("img", image, Files.probeContentType(Path.of(email.getImagePath())));
                html.append("<img src='cid:img'/>");
            }

            if (email.getImagePath() != null) {
                Resource image = storageService.load(email.getImagePath());
                helper.addInline("img", image, Files.probeContentType(Path.of(email.getImagePath())));
            }

            if (email.getAttachments() != null) {
                for (String path : email.getAttachments()) {
                    Resource file = storageService.load(path);
                    helper.addAttachment(Path.of(path).getFileName().toString(), file);
                }
            }

            helper.setText(html.toString(), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException | IOException e) {
            email.setStatus("FAILED");
            email.setError(e.getMessage());
        } finally {
            emailHistoryRepository.save(email);
        }
    }

}
