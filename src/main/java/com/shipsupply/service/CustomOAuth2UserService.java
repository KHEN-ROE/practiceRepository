package com.shipsupply.service;

import com.shipsupply.domain.User;
import com.shipsupply.persistence.UserRepository;
import com.shipsupply.security.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        logger.info("OAuth2User loadUser 호출");
        
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oauth2User.getAttribute("sub");  // "sub"는 Google에서 제공하는 사용자의 고유 ID.
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");  // 사용자의 이름 추출

        // 이미 가입한 사용자인지 확인
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    // 새로운 사용자 등록
                    User newUser = new User();
                    newUser.setId(email != null ? email : "default_id");
                    newUser.setPassword(encoder.encode(email));
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);
                    newUser.setEmail(email);
                    newUser.setRole("USER");
                    newUser.setUsername(name);

                    return userRepository.save(newUser); // db에 저장
                });

        return UserPrincipal.create(user, oauth2User.getAttributes()); //소셜 계정으로  로그인하면 유저 상세정보 생성
    }
}
