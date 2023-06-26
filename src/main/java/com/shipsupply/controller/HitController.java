package com.shipsupply.controller;

import com.shipsupply.domain.Hit;
import com.shipsupply.service.HitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hit")
public class HitController {

    private static final Logger logger = LoggerFactory.getLogger(HitController.class);

    @Autowired
    HitService hitService;

    @GetMapping("/get/{commentId}")
    public Long getHit(@PathVariable Long commentId) {
        return hitService.getHit(commentId);
    }

    @PostMapping("/add")
    public Hit addHit(@RequestBody Hit hit) {
        logger.info("받은 hit 정보 : " + hit);
        return hitService.addHit(hit);
    }

    @DeleteMapping("/delete")
    public void deleteHit(@RequestBody Hit hit) {
        hitService.deleteHit(hit);
    }
}
