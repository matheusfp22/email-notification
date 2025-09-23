package org.example.emailnotification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.emailnotification.dto.request.EmailMimeRequestDto;
import org.example.emailnotification.dto.request.EmailSimpleRequestDto;
import org.example.emailnotification.entity.EmailHistoryEntity;
import org.example.emailnotification.repository.EmailHistoryRepository;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailHistoryRepository emailHistoryRepository;

    public EmailService(JavaMailSender mailSender, EmailHistoryRepository emailHistoryRepository) {
        this.mailSender = mailSender;
        this.emailHistoryRepository = emailHistoryRepository;
    }

    public void sendSimpleEmail(EmailSimpleRequestDto request) {
        EmailHistoryEntity emailHistory = EmailHistoryEntity.builder()
                .receiver(request.getTo())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("noreply@mail.com");
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getMessage());

            mailSender.send(message);
        } catch (MailException m) {
            emailHistory.setStatus("FAILED");
            emailHistory.setError(m.getMessage());
        } finally {
            emailHistoryRepository.save(emailHistory);
        }

    }

    public void sendMimeEmail(EmailMimeRequestDto request) {
        EmailHistoryEntity emailHistory = EmailHistoryEntity.builder()
                .receiver(request.getTo())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();

        try {
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
        } catch (MessagingException | MailException m) {
            emailHistory.setStatus("FAILED");
            emailHistory.setError(m.getMessage());
        } finally {
            emailHistoryRepository.save(emailHistory);
        }
    }

}
