package com.shipsupply.controller;

import com.shipsupply.domain.Comment;
import com.shipsupply.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @GetMapping("/get/{boardId}")
    public List<Comment> getComment(@PathVariable Long boardId) {
        logger.info("글번호 : " + boardId);
        return commentService.getComment(boardId);
    }

    @PostMapping("/add")
    public Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }


    @PutMapping("/update/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        logger.info("받은 댓글 정보 : " +  id + "," + comment);
        return commentService.updateComment(id, comment);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable Long id, @RequestBody Comment comment) {
        logger.info("받은 정보 : " + id + "," + comment);
        commentService.deleteComment(id, comment);
    }
}
