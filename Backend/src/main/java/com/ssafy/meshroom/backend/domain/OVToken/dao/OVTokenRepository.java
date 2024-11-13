package com.ssafy.meshroom.backend.domain.OVToken.dao;

import com.ssafy.meshroom.backend.domain.OVToken.domain.OVToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OVTokenRepository extends MongoRepository<OVToken, String> {
    List<OVToken> findAllBySessionSid(String sessionSid);

    List<OVToken> findAllByUserSid(String userSid);

    void deleteAllBySessionSid(String sessionSid);

    void deleteBySessionSidAndUserSid(String sessionSid, String userSid);

    boolean existsByUserSidAndSessionSid(String userSid, String sessionSid);

    Optional<OVToken> findByOvToken(String ovToken);

}