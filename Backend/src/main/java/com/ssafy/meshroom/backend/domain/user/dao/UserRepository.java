package com.ssafy.meshroom.backend.domain.user.dao;

import com.ssafy.meshroom.backend.domain.user.domain.User;
import com.ssafy.meshroom.backend.domain.user.domain.UserRole;
import com.ssafy.meshroom.backend.domain.user.dto.UserNameInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ '_id' : ?0 }")
    @Update(" {'$set': { 'role' : ?1 }} ")
    void updateById(String _id, UserRole role);

    UserNameInfo findBy_id(String _id);

//    // username으로 사용자 찾기
//    Optional<User> findByUsername(String username);
//
//    // token으로 사용자 찾기
//    Optional<User> findByToken(String token);
//
//    // 특정 역할을 가진 모든 사용자 찾기
//    List<User> findAllByRole(UserRole role);
//
//    // username이 특정 문자열로 시작하는 사용자 찾기
//    List<User> findByUsernameStartingWith(String prefix);
//
//    // username과 role로 사용자 찾기
//    Optional<User> findByUsernameAndRole(String username, UserRole role);
//
//    // 사용자 존재 여부 확인
//    boolean existsByUsername(String username);
//
//    // username으로 사용자 삭제
//    void deleteByUsername(String username);
//
//    // 특정 역할을 가진 사용자 수 계산
//    long countByRole(UserRole role);
}
