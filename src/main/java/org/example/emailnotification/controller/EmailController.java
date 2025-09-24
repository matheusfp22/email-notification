package org.example.emailnotification.controller;

import jakarta.validation.Valid;
import org.example.emailnotification.dto.request.EmailMimeQueueRequestDto;
import org.example.emailnotification.dto.request.EmailMimeRequestDto;
import org.example.emailnotification.dto.request.EmailSimpleRequestDto;
import org.example.emailnotification.producer.EmailProducer;
import org.example.emailnotification.service.StorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("email")
public class EmailController {

    private final EmailProducer emailProducer;
    private final StorageService storageService;

    public EmailController(EmailProducer emailProducer, StorageService storageService) {
        this.emailProducer = emailProducer;
        this.storageService = storageService;
    }

    @PostMapping("send-simple")
    public ResponseEntity<String> sendSimpleEmail(@RequestBody @Valid EmailSimpleRequestDto request) {
        emailProducer.sendEmail(request);
        return ResponseEntity.ok("Email added to sending queue.");
    }

    @PostMapping(value = "send-mime", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendMimeEmail(@ModelAttribute @Valid EmailMimeRequestDto request) {
        String imagePath = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            imagePath = storageService.save(request.getImage());
        }

        List<String> attachmentPaths = new ArrayList<>();
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            for (MultipartFile file : request.getAttachments()) {
                if (!file.isEmpty()) {
                    attachmentPaths.add(storageService.save(file));
                }
            }
        }

        EmailMimeQueueRequestDto message = EmailMimeQueueRequestDto.builder()
                .to(request.getTo())
                .subject(request.getSubject())
                .message(request.getMessage())
                .imagePath(imagePath)
                .attachments(attachmentPaths)
                .build();

        emailProducer.sendEmail(message);
        return ResponseEntity.ok("Email added to sending queue.");
    }

}
