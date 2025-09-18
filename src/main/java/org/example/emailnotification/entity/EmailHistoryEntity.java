package org.example.emailnotification.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter

@Document(collection = "email-history")
public class EmailHistoryEntity {

    @Id
    private String id;

    private String receiver;

    private String subject;

    private String message;

    private String status;

    private String sentAt;

}
