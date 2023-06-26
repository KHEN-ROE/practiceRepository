package com.shipsupply.service;

import com.shipsupply.domain.Board;
import com.shipsupply.domain.User;
import com.shipsupply.persistence.BoardRepository;
import com.shipsupply.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class BoardService {

    @Autowired
    BoardRepository br;

    @Autowired
    UserRepository ur;

    public Page<Board> getList(Pageable pageable) {
        return br.findAll(pageable);
    }

    public Board getBoard(Long id) {
        Optional<Board> findBoard = br.findById(id);
        Board b = new Board();
        if(findBoard.isPresent()) {
            b = findBoard.get();
        }
        return b;
    }

    public Board addBoard(Board board) {
        String id = board.getUser().getId();
        Optional<User> findUser  = ur.findById(id);
        if (findUser.isPresent()) {
            User u = findUser.get();
            board.setUser(u); // user엔티티와 board 엔티티의 관계를 설정해준다.
                                //// db에 저장될 때 Board테이블의 user컬럼에 해당 사용자의 id를 외래키로 저장
            board.setDate(new Date());
            return br.save(board);
        }else {
            throw new RuntimeException("해당하는 사용자 없음");
        }
    }

    public Board updateBoard(Long id, Board board) {
        Board b;
        Optional<Board> findBoard = br.findById(id);
        if(findBoard.isPresent()) {
            b = findBoard.get();
            if(b.getUser().getId().equals(board.getUser().getId())) {
                Optional.ofNullable(board.getTitle()).ifPresent(b::setTitle); //board.getTitle()이 null이 아닌지 체크(ofNullable)
                                                                                // Optional 객체가 값이 존재하면(ifPresent) b의 title을(b::setTitle) board.getTitle()로 변경
                                                                                // if문으로 바꿀 수 있다.
                                                                                //if(board.getTitle() != null) {
                                                                                //b.setTitle(board.getTitle()); }
                Optional.ofNullable(board.getText()).ifPresent(b::setText);
                Optional.ofNullable(board.getDate()).ifPresent(b::setDate);
                return br.save(b);
            } else {
                throw new RuntimeException("일치하지 않는 사용자");
            }
        } else {
            throw new RuntimeException("존재하지 않는 게시글");
        }
    }

    public void deleteBoard(Long id, Board board) {
        Optional<Board> findBoard = br.findById(id);
        if(findBoard.isPresent()) {
            Board b = findBoard.get();
            if(b.getUser().getId().equals(board.getUser().getId())) {
                br.deleteById(id);
            } else {
                throw new RuntimeException("일치하지 않는 사용자");
            }
        } else {
            throw new RuntimeException("존재하지 않는 게시글");
        }
    }
}
