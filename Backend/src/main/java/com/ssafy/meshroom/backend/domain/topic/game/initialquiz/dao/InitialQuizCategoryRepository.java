package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dao;

import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.domain.InitialQuizCategory;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InitialQuizCategoryRepository extends MongoRepository<InitialQuizCategory, String> {
    @Aggregation(pipeline = {
            "{ $sample: { size: 1 } }"
    })
    Optional<InitialQuizCategory> findRandomCategory();

}
