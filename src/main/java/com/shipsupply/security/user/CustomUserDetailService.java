package com.shipsupply.security.user;


import com.shipsupply.domain.User;
import com.shipsupply.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

//사용자 정보를 데이터베이스에서 불러오는 데 사용
@Service
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    @Autowired
    UserRepository userRepository;

    //loadUserByUsername 메서드는 사용자 아이디를 매개변수로 받아서 회원 정보를 조회하고, 
    // 조회 결과를 UserDetails 타입의 객체로 변환해서 리턴
    @Override
    public UserDetails loadUserByUsername(String email) {
        
        logger.info("loadUserByUsername 호출");
        User u = new User();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            u = user.get();
        }
        return new UserPrincipal(u); //UserPrincipal은 UserDetails를 구현한 클래스
    }

}
