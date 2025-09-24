package org.example.emailnotification.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class EmailMimeQueueRequestDto implements EmailMessage {

    private String to;
    private String subject;
    private String message;
    private String imagePath;
    private List<String> attachments;

}
