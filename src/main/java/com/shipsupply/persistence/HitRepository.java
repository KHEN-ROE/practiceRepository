package com.shipsupply.persistence;

import com.shipsupply.domain.Comment;
import com.shipsupply.domain.Hit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 이 레포지토리 필요한 이유? 좋아요 한 번만 누를 수 있게 만드려고(아이디 조회해서 좋아요 눌렀는지 확인)
public interface HitRepository extends JpaRepository<Hit, Long> {
//        Long countByCommentNum(Long num);
    Optional<Hit> findByUserIdAndCommentId(String id, Long id2);
}
