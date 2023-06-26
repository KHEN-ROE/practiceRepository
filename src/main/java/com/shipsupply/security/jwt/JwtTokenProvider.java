package com.shipsupply.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    UserDetailsService userDetailsService;

    //    private static final SecretKey secretKey = Keys
//            .hmacShaKeyFor("ASDasdasd1dasd_dqwdqw1dsad789".getBytes(StandardCharsets.UTF_8));
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(
            Base64.getEncoder().encode("ASDasdasd1dasd_dqwdqw1dsad789".getBytes(StandardCharsets.UTF_8))
    );

    // Jwt 토큰 생성
    public static String createToken(String userPk, String role) {
        logger.info("createToken 호출");
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("role", role); // 토큰에 role을 추가. 나중에 토큰을 디코딩할 때 role을 검증하려고

        Date now = new Date();
        // 1시간 토큰 유효(1000L * 60 * 60) - 1000밀리초 * 60분 * 60초
        long tokenValidMillisecond = 1000L * 60 * 60;
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond)) // 토큰 유효시간 설정
                .signWith(secretKey)
                .compact();
    }

    public String resolveTokenFromCookie(HttpServletRequest request) {
        logger.info("resolveTokenFromCookie 호출");
        logger.info("받은 쿠키 : " + request);
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if("Authorization".equals(cookie.getName())) {
                    logger.info("쿠키에 있는 토큰 : " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        logger.info("validateToken 호출");
        try {
            // parseClaimsHws 메서드가 디코딩 및 파싱
            // setSigninKey로 넘어온 시크릿을 이용해 헤더와 페이로드를 서명한 후 token의 서명과 비교
            // 위조되지 않았다면 페이로드(claims) 리턴
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            logger.info("유효하지 않은 토큰");
            return false;
        }
    }

    // Jwt 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token) {
        logger.info("getAuthentication 호출");
        // 토큰에서 권한 정보를 가져와서 권한 목록을 생성.
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

        Collection<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + claims.get("role").toString()));
        logger.info("getAuthentication의 authorities :" + authorities);
        org.springframework.security.core.userdetails.User principal =
                new org.springframework.security.core.userdetails.User(this.getUserPk(token), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());

    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        logger.info("getUserPk 호출");
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }
}