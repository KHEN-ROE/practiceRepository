package com.shipsupply.security.user;

import com.shipsupply.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

// 로그인한 사용자의 상세정보 나타내는 클래스. 소셜 로그인할 때 호출됨.oauth2service에서 호출하기 때문. 근데 db기반 로그인할 때는 안쓰네?
public class UserPrincipal implements UserDetails, OAuth2User {

    private static final Logger logger = LoggerFactory.getLogger(UserPrincipal.class);

    private final User user;
    private Map<String, Object> attributes;

    public UserPrincipal(User user) {
        logger.info("UserPrincipal 생성자1 호출");
        this.user = user;
    }

    public UserPrincipal(User user, Map<String, Object> attributes) {
        logger.info("UserPrincipal 생성자2 호출");
        this.user = user;
        this.attributes = attributes;
    }

    public User getUser() {
        logger.info("getUser 호출");
        return user;
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        logger.info("UserPrincipal create 호출");
        UserPrincipal userPrincipal = new UserPrincipal(user, attributes);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public Map<String, Object> getAttributes() {
        logger.info("getAttributes 호출");
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        logger.info("setAttributes 호출");
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        logger.info("getName 호출");
        return user.getId();
    }

    //사용자가 가지고 있는 권한(롤)을 반환하는 메서드.
    //Spring Security에서는 권한을 GrantedAuthority 인터페이스를 통해 표현
    //예를 들어, 'ROLE_USER', 'ROLE_ADMIN' 등이 될 수 있음
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        logger.info("UserPrincipal getAuthorities 호출");
//        Set<SimpleGrantedAuthority> role = Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        logger.info("getPassword 호출");
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        logger.info("getUsername 호출");
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        logger.info("isAccountNonExpired 호출");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        logger.info("isAccountNonLocked 호출");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        logger.info("isCredentialsNonExpired 호출");
        return true;
    }

    @Override
    public boolean isEnabled() {
        logger.info("isEnabled 호출");
        return true;
    }
}
