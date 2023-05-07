package edu.pnu.controller;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
public class AuthController {

	@GetMapping("/loginSuccess")
	public RedirectView loginSuccess(@AuthenticationPrincipal OAuth2User principal) { // defaultSuccessUrl()에서 바로 3000으로
																						// 리디렉션 할 수 있는데
																						// 굳이 여기 온 이유는 사용자 정보 로그 찍어보려고.
																						// 여기서 로그 찍고 다시 리디렉트 시켜줌
		// 사용자 정보를 처리하거나 필요한 작업을 수행합니다.
		System.out.println("사용자 정보: " + principal.getAttributes());

		// JWT 생성
		String JWT = createJWT(principal);

		// JWT 디코딩 및 "aud" 클레임 값 출력
		SecretKey secretKey = Keys
				.hmacShaKeyFor("secreykey".getBytes(StandardCharsets.UTF_8));
		JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
		Jws<Claims> jws = parser.parseClaimsJws(JWT);
		String audClaim = jws.getBody().get("aud", String.class);
		System.out.println("Audience claim in the token: " + audClaim);

		RedirectView redirectView = new RedirectView("http://localhost:3000?token=" + JWT);
//		redirectView.setUrl("http://localhost:3000?token=" + token);
		return redirectView;
	}

	private String createJWT(OAuth2User principal) {
		// JWT 생성 로직 구현 (예: 토큰 만료 시간, 서명 등)
		// 사용자 정보에서 필요한 부분을 가져와 토큰에 포함시키세요.
		// 예를 들어, 사용자의 이메일을 토큰의 subject로 사용할 수 있습니다.
		// 이 예제에서는 간단한 예시로 생성하지만, 실제 환경에서는 더 많은 정보와 보안을 고려해야 합니다.
		String email = principal.getAttribute("email");
		String name = principal.getAttribute("name");
	    String locale = principal.getAttribute("locale");
		Instant now = Instant.now();
		Instant expiry = now.plus(1, ChronoUnit.HOURS);

		SecretKey secretKey = Keys
				.hmacShaKeyFor("secretkey".getBytes(StandardCharsets.UTF_8));
		final String AUDIENCE = "client_id";

		String jwt = Jwts.builder().setSubject(email)
				.claim("name", name) 
				.claim("locale", locale) //토큰에 이메일, 이름, 지역 추가
				.setIssuer("https://accounts.google.com").setAudience(AUDIENCE) // 클라이언트 ID를 audience로 추가																														
				.setIssuedAt(Date.from(now)).setExpiration(Date.from(expiry))
				.signWith(secretKey) // 생성된 키를 사용하여 서명합니다.
				.compact();
		System.out.println("Generated JWT: " + jwt);

		return jwt;
	}

//	@GetMapping("/loginFailure")
//	public RedirectView loginFailure() {
//		redirectView.setUrl("http://localhost:3000");
//		return redirectView;
//	}
}
