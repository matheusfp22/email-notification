package org.example.emailnotification.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSimpleRequestDto implements EmailMessage {

    private String to;
    private String subject;
    private String message;

}
