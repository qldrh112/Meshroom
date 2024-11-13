package com.ssafy.meshroom.backend.domain.session.dao;

import com.ssafy.meshroom.backend.domain.session.domain.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
    Optional<Session> findBySessionId(String sessionId);
    Optional<List<Session>> findAllByMainSession(String sessionId);
    Optional<List<Session>> deleteAllByMainSession(String sessionId);
}
