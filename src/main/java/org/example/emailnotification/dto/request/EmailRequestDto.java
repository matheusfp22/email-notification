package org.example.emailnotification.dto.request;

import lombok.Getter;

@Getter

public class EmailRequestDto {

    private String from;
    private String to;
    private String subject;
    private String message;

}
