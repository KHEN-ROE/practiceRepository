package com.shipsupply.service;

import com.shipsupply.domain.Board;
import com.shipsupply.domain.Comment;
import com.shipsupply.domain.User;
import com.shipsupply.persistence.BoardRepository;
import com.shipsupply.persistence.CommentRepository;
import com.shipsupply.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    BoardRepository boardRepository;

    // 댓글은 모든 사람이 다 볼 수 있어야 함. 근데 비밀 댓글인 경우?(이건 나중에) + 대댓글은?
    public List<Comment> getComment(Long boardId) {
        return commentRepository.findAllByBoardIdOrderByHitCountDesc(boardId);
    }

    public Comment addComment(Comment comment) {
        // 댓글 작성자가 db에 있는 회원인지 확인
        Optional<User> findUser = userRepository.findById(comment.getUser().getId());
        if (findUser.isPresent()) {
            User user = findUser.get();
            if(user.getId().equals(comment.getUser().getId())){
                return commentRepository.save(comment);
            }
            else {
                throw new RuntimeException("권한이 없습니다");
            }
        }else {
            throw new RuntimeException("존재하지 않는 회원");
        }
    }

    public Comment updateComment(Long id, Comment comment) {
        Comment c;
        Optional<Board> findBoard = boardRepository.findById(comment.getBoard().getId()); // 일단 게시판을 찾는다
        if(findBoard.isPresent()) {
            Board board = findBoard.get();
            Optional<Comment> findComment = commentRepository.findById(id); // 그 다음 댓글을 찾는다.
            if(findComment.isPresent()) {
                c = findComment.get();
                if(c.getUser().getId().equals(comment.getUser().getId())) {
                    Optional.ofNullable(comment.getText()).ifPresent(c::setText);
                    Optional.ofNullable(comment.getDate()).ifPresent(c::setDate);
                    return commentRepository.save(c);
                } else {
                    throw new RuntimeException("일치하지 않는 사용자");
                }
            } else {
                throw new RuntimeException("존재하지 않는 댓글");
            }
        } else {
            throw new RuntimeException("존재하지 않는 게시판");
        }
    }

    public void deleteComment(Long id, Comment comment) {
        Optional<Board> findBoard = boardRepository.findById(comment.getBoard().getId());
        if(findBoard.isPresent()) {
            Board board = findBoard.get();
            Optional<Comment> findComment = commentRepository.findById(id);
            if(findComment.isPresent()) {
                Comment c = findComment.get();
                if(c.getUser().getId().equals(comment.getUser().getId())) {
                    commentRepository.deleteById(id);
                } else {
                    throw new RuntimeException("일치하지 않는 사용자");
                }
            } else {
                throw new RuntimeException("존재하지 않는 댓글");
            }
        } else {
            throw new RuntimeException("존재하지 않는 게시글");
        }
    }

}
