package com.backend.repository;

import com.backend.dto.FeedBack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentimentRepository extends MongoRepository<FeedBack, String> {
}
