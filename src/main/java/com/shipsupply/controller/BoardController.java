package com.shipsupply.controller;

import com.shipsupply.domain.Board;
import com.shipsupply.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    BoardService bs;

    // Page<Board> -> Page 객체로 감싸서 반환하면,
    // 클라이언트에서는 해당 페이지의 게시글 데이터뿐만 아니라 전체 페이지 수, 현재 페이지 번호 등의 페이지네이션 정보도 함께 받을 수 있음
    // 클라이언트의 요청에 따른 페이지네이션 처리를 위해 Pageable을 활용
    @GetMapping("/view")
    public Page<Board> getList(@RequestParam int page, @RequestParam int size) { // 요청된 페이지 번호, 한 페이지에 보여줄 게시글 개수
        logger.info("page : " + page + "," + "size : " + size);
        Pageable pageable = PageRequest.of(page, size); //page와 size를 기반으로 한 Pageable 객체 생성
        return bs.getList(pageable);
    }

    @GetMapping("/view/{id}")
    public Board getBoard(@PathVariable Long id) {
        logger.info("글 번호 : " + id);
        return bs.getBoard(id);
    }

    @PostMapping("/add")
    public Board addBoard(@RequestBody Board board) {
        logger.info("받은 게시글 정보 : {}", board);
        return bs.addBoard(board);
    }

    @PutMapping("/update/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody Board board) {
        logger.info("받은 정보 : " + id  + "," + board);
        return bs.updateBoard(id, board);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBoard(@PathVariable Long id, @RequestBody Board board) {
        bs.deleteBoard(id, board);
    }
}
