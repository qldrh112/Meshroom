package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dao;

import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.domain.InitialQuizInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InitialQuizRepository extends MongoRepository<InitialQuizInfo, String> {
    boolean existsByOvTokenAndSessionId(String ovToken, String sessionId);
    Optional<InitialQuizInfo> findByOvTokenAndSessionId(String ovToken, String sessionId);
    Optional<List<InitialQuizInfo>> findAllBySessionId(String sessionId);
}
