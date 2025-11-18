package com.musat.musat_backend.repository;

import com.musat.musat_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    // [추가] 학번으로 유저 찾기 (단건)
    Optional<User> findByStudentId(String studentId);

    // [추가] 학번 리스트로 유저 목록 찾기 (IN 쿼리)
    List<User> findAllByStudentIdIn(List<Integer> studentIds);
}
