package org.example.emailnotification.repository;

import org.example.emailnotification.entity.EmailHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailHistoryRepository extends MongoRepository<EmailHistoryEntity, String> {
}
