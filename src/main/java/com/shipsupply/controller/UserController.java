package com.shipsupply.controller;

import com.shipsupply.domain.User;
import com.shipsupply.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    // ResponseEntity는 스프링 프레임워크에서 제공하는 클래스. http 응답의 상태코드, 헤더, 본문 등을 포함하는 정보를 갖는 컨테이너
    //이 클래스를 사용하면 컨트롤러에서 http 응답을 상세하게 조작가능
    //http 요청에 대한 응답을 생성하는 역할
    //회원 정보 조회(관리자만 가능)
    @GetMapping("/inquire")
    public User inquire(@RequestParam String id) {
        return userService.inquire(id);
    }

    @PostMapping("/join")
    public User join(@RequestBody User user) {
        return userService.join(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        logger.info("로그인 컨트롤러 호출");

        Map<String, Object> map = userService.login(user);

        String token = (String) map.get("token");
        logger.info("생성한 토큰 : " + token);

        Cookie tokenCookie = new Cookie("Authorization", token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/"); // 쿠키의 경로를 모든 경로로 설정
        tokenCookie.setDomain("15.164.163.107"); // 클라이언트 도메인 설정
        response.addCookie(tokenCookie);
        logger.info("리턴할 response : " + response);

        return ResponseEntity.ok(map);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody User user, HttpServletResponse response) {
        logger.info("logout 호출");
        Cookie tokenCookie = new Cookie("Authorization", null); // 토큰 담겨있는 HttpOnly쿠키 삭제
        tokenCookie.setMaxAge(0); // 쿠키 즉시 만료되도록 설정
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/"); // 쿠키의 경로를 모든 경로로 설정

        Cookie userIdCookie = new Cookie("userId", null); // userId 있는 쿠키 삭제
        userIdCookie.setMaxAge(0);
        userIdCookie.setPath("/");

        response.addCookie(tokenCookie); // 응답에 쿠키 추가
        response.addCookie(userIdCookie);

        return ResponseEntity.ok().build(); // 상태코드 200과 빈 본문을 가진 응답 반환
    }


    @PutMapping("/update")
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody User user) {
        userService.delete(user);
    }
}
