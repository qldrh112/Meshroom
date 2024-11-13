package com.ssafy.meshroom.backend.domain.contents.dao;

import com.ssafy.meshroom.backend.domain.contents.domain.ContentsOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentsOrderRepository extends MongoRepository<ContentsOrder, String> {
    List<ContentsOrder> findAllBySessionId(String sessionSid);

    @Query("{ '_id' : ?0 }")
    @Update(" {'$set': { 'isDone' : ?1 }} ")
    void updateById(@Param("_id") String _id,@Param("isDone") Boolean isDone);

}
