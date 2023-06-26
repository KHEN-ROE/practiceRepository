package com.shipsupply.service;

import com.shipsupply.domain.Comment;
import com.shipsupply.domain.Hit;
import com.shipsupply.persistence.BoardRepository;
import com.shipsupply.persistence.CommentRepository;
import com.shipsupply.persistence.HitRepository;
import com.shipsupply.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HitService {

    @Autowired
    HitRepository hitRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CommentRepository commentRepository;

    public Long getHit(Long commentId) {
        return commentRepository.findById(commentId).get().getHitCount();
    }

    public Hit addHit(Hit hit) {
        Optional<Hit> existingHit = hitRepository.findByUserIdAndCommentId(hit.getUser().getId(), hit.getComment().getId());
        if (existingHit.isPresent()) {
            throw new RuntimeException("좋아요는 한번만 가능합니다");
        } else {
            Comment comment = commentRepository.findById(hit.getComment().getId()).get();
            comment.setHitCount(comment.getHitCount() + 1);
            commentRepository.save(comment);
        }
        return hitRepository.save(hit);
    }

    public void deleteHit(Hit hit) {
        Comment comment = commentRepository.findById(hit.getComment().getId()).get();
        comment.setHitCount(comment.getHitCount() - 1);
        commentRepository.save(comment);

        hitRepository.delete(hit);
    }
}
