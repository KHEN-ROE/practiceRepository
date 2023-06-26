package com.shipsupply.security.config;

import com.shipsupply.security.jwt.JwtAuthenticationFilter;
import com.shipsupply.security.jwt.JwtTokenProvider;
import com.shipsupply.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.shipsupply.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //(debug = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomOAuth2UserService customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // rest api이므로 기본설정 안함. 기본설정은 비인증 시 로그인 폼 화면으로 리다이렉트 된다.
                .httpBasic().disable()
                .csrf().disable()
                // jwt token으로 생성하므로 세션은 필요 없으므로 생성 안함.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                //  Spring Security 필터 체인을 통과하지만
                //인증 없이도 접근이 허용됨. 사용자는 이 경로로 요청을 보낼 때 별도로 인증 절차를 거치지 않아도 됨
                .antMatchers("/", "/oauth2/**", "/api/user/join", "/api/user/login", "/api/user/logout").permitAll()
                //사용자의 권한은 사용자가 인증 과정을 거치면서 생성된 UserDetails 객체의 getAuthorities() 메서드를 통해 얻어짐 
                // 이 메서드는 사용자의 권한을 나타내는 GrantedAuthority 객체의 컬렉션을 반환함
                // 따라서, hasAnyRole("USER", "ADMIN") 설정은 실제로는 사용자의 GrantedAuthority 목록에 
                // "ROLE_USER" 혹은 "ROLE_ADMIN"이 포함되어 있는지 확인하는 것임
                .antMatchers("/api/board/**", "/api/comment/**", "/api/item/**").hasAnyRole("USER", "ADMIN") // 이 메서드는 내부적으로 "ROLE_" 접두사를 자동으로 추가
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                // 그 외 나머지 요청은 모두 인증된 회원만 접근 가능
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureUrl("/loginFailure")
                .and()
                // jwt token 필터를 id/password 인증 필터 전에 넣는다.
                // JWT 토큰을 이용한 인증을 처리하기 위한 필터
                // 요청이 들어올 때마다 JWT 토큰을 확인하여 사용자를 인증.
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), // antMatchers().permitAll()에 있는 요청들은 이 필터 거치지 않는다
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
