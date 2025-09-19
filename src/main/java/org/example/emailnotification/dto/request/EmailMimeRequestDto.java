package org.example.emailnotification.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EmailMimeRequestDto {

    private String to;
    private String subject;
    private String message;
    private MultipartFile image;
    private List<MultipartFile> attachments;

}
