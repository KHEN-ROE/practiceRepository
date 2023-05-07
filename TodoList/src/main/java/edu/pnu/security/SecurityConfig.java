package edu.pnu.security;


import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import io.jsonwebtoken.security.Keys;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable().cors().and()
            .authorizeRequests()
                .antMatchers("/", "/oauth2/**").permitAll() 
                .antMatchers("/api/todos").authenticated()// "/api/todos"에 대한 POST 요청을 인증된 사용자에게만 허용
                .anyRequest().authenticated()
            .and()
            .oauth2Login()
                .defaultSuccessUrl("/loginSuccess", true)
                .failureUrl("/loginFailure")
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 URL을 설정
                .logoutSuccessUrl("http://localhost:3000") // 로그아웃 후 리다이렉트할 URL을 설정
                .invalidateHttpSession(true) // 로그아웃 시 HTTP 세션을 무효화
                .deleteCookies("JSESSIONID"); // 로그아웃 시 쿠키를 삭제
        http.oauth2ResourceServer()
        .jwt()
        .decoder(jwtDecoder())
        .jwtAuthenticationConverter(jwtAuthenticationConverter());
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }
    
      
//    @Bean
//    JwtDecoder jwtDecoder() {
//        SecretKey secretKey = Keys.hmacShaKeyFor("your_own_secretkey".getBytes(StandardCharsets.UTF_8));
//        final String AUDIENCE = "your_own_client_id";
//
//        
//        
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
//        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer("https://accounts.google.com");
////    	aud 검증에 문제가 있어서 생략을 했더니 통신이 됐다. 그러나 불완전한 코드이므로 아래의 메서드로 대체한다.
////      OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<>("aud", aud -> aud.equals(AUDIENCE));
////      OAuth2TokenValidator<Jwt> combinedValidators = new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience);
//        OAuth2TokenValidator<Jwt> combinedValidators = new DelegatingOAuth2TokenValidator<>(withIssuer);
//        jwtDecoder.setJwtValidator(combinedValidators);
//        
//        return jwtDecoder;
//    }
    
    @Bean
    JwtDecoder jwtDecoder() {
        SecretKey secretKey = Keys.hmacShaKeyFor("your_own_secreykey".getBytes(StandardCharsets.UTF_8));
        
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).build();

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer("https://accounts.google.com");

        OAuth2TokenValidator<Jwt> withAudience = new OAuth2TokenValidator<Jwt>() {
        	@Override
            public OAuth2TokenValidatorResult validate(Jwt jwt) {
                List<String> audList = jwt.getClaim("aud");
                String expectedAud = "your_own_client_id";
                if (audList != null && audList.contains(expectedAud)) {
                    return OAuth2TokenValidatorResult.success();
                } else {
                    System.out.println("Failed aud claim validation. Expected: " + expectedAud + ", Actual: " + audList);
                    return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_aud", "The aud claim is not valid", null));
                }
            }
        };

        OAuth2TokenValidator<Jwt> combinedValidators = new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience);
        jwtDecoder.setJwtValidator(combinedValidators);

        return jwtDecoder;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter());
        return converter;
    }
}

