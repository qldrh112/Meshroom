package com.ssafy.meshroom.backend.domain.contents.dao;

import com.ssafy.meshroom.backend.domain.contents.domain.Contents;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentsRepository extends MongoRepository<Contents, String> {
    
    
}
