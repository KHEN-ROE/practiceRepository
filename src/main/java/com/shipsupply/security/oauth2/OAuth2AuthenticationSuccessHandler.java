package com.shipsupply.security.oauth2;

import com.shipsupply.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        logger.info("onAuthenticationSuccess 호출");
        String userRole = ((GrantedAuthority) authentication.getAuthorities().toArray()[0]).getAuthority(); // 첫 번째 권한을 가져옴(문자열로)
        String token = JwtTokenProvider.createToken(authentication.getName(), userRole);
        logger.info("생성한 oauth2 토큰: {}" , token);
//         헤더에 토큰 포함하여 전달(토큰 전달 방법은 1. 헤더에 포함 2. 쿠키에 포함 3. 바디에 포함 3개가 있음)

        // HttpOnly 쿠키에 토큰 추가
        Cookie tokenCookie = new Cookie("Authorization", token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/"); // 모든 경로에서 쿠키 접근 허용
        response.addCookie(tokenCookie);

        // HttpOnly 쿠키가 아닌 쿠키에 userId 추가
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userId = oAuth2User.getAttribute("email"); //"sub"는 고유 사용자 아이디를 나타내는 표준 claim
        logger.info("소셜 계정 ID : " + userId);
        Cookie userIdCookie = new Cookie("userId", userId);
        logger.info("userIdCookie : " + userIdCookie);
        userIdCookie.setHttpOnly(false);
        userIdCookie.setPath("/");
        userIdCookie.setDomain("localhost"); // 클라이언트 도메인 설정. 없어도 되는듯
        response.addCookie(userIdCookie);

        clearAuthenticationAttributes(request); //인증 과정에서 저장된 세션을 정리

        String redirectUrl = "http://15.164.163.107:3000";

        // 응답을 리다이렉트 URL로 보냄
        response.sendRedirect(redirectUrl);
    }
}
