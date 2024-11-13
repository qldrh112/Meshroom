package com.ssafy.meshroom.backend.domain.session.dao;

import com.ssafy.meshroom.backend.domain.session.domain.GroupName;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupNameRepository extends MongoRepository<GroupName, String> {
    @Aggregation(pipeline = {
            "{ $sample: { size: 1 } }"
    })
    Optional<GroupName> findRandomGroupName();
}
