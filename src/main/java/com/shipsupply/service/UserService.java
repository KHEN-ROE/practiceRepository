package com.shipsupply.service;

import com.shipsupply.domain.User;
import com.shipsupply.persistence.UserRepository;
import com.shipsupply.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository ur;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User inquire(String id) {
        Optional<User> findUser = ur.findById(id);
        User u = new User();
        if (findUser.isPresent()) {
            u = findUser.get();
            if (u.getRole().equals("admin")) {
                return u;
            }
        }
        return null;
    }

    public User join(User user) {
        Optional<User> findUser = ur.findById(user.getId());
        Optional<User> findEmail = ur.findByEmail(user.getEmail());
        if (findUser.isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디");
        } else if (findEmail.isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일");
        } else {
            User u = new User();
            u.setId(user.getId());
            u.setEmail(user.getEmail());
            u.setPassword(encoder.encode(user.getPassword()));
            u.setUsername(user.getUsername());
            u.setRole(user.getRole());
            return ur.save(u);
        }
    }

    public Map<String, Object> login(User user) {
        Map<String, Object> map = new HashMap<>();

        Optional<User> findUser = ur.findById(user.getId());
        if (findUser.isPresent()) {
            User u = findUser.get();
            if (u.isDeleted()) {
                throw new RuntimeException("탈퇴한 회원");
            } else {
                if (encoder.matches(user.getPassword(), u.getPassword())) {
                    logger.info("userService에서 createToken 호출");
                    String token = JwtTokenProvider.createToken(u.getUsername(), u.getRole());
                    logger.info("생성한 토큰 : {}", token);
                    map.put("token", token);
                    map.put("userId", u.getId());

                    return map;
                } else {
                    throw new RuntimeException("비밀번호 불일치");
                }
            }
        } else {
            throw new RuntimeException("존재하지 않는 회원");
        }
    }

    public User update(User user) {
        Optional<User> findUser = ur.findById(user.getId());
        if (findUser.isPresent()) {
            User u = findUser.get();
            if (encoder.matches(user.getPassword(), u.getPassword())) {
                // ofNullable -> null이 아니면 ()안의 값으로 변경. null이면 Optional.empty() 반환
                // ifPresent -> 값이 있으면() 안의 값 사용. 여기서는 기존값 그대로 둠. ::는 메서드 레퍼런스
                Optional.ofNullable(user.getNewPassword()).ifPresent(p -> u.setPassword(encoder.encode(p))); // p는 임시 매개변수 이름. 딴걸로 바꿔도 됨
                // 람다식 쓴 이유는 암호화 때문에
                Optional.ofNullable(user.getEmail()).ifPresent(u::setEmail); // setEmail 메서드 직접참조 (email) -> u.setEmail(email)과 동일
                Optional.ofNullable(user.getUsername()).ifPresent(u::setUsername);
                Optional.ofNullable(user.getRole()).ifPresent(u::setRole);
                Optional.ofNullable(user.getProvider()).ifPresent(u::setProvider);
                Optional.ofNullable(user.getProviderId()).ifPresent(u::setProviderId);
                return ur.save(u);
            } else {
                throw new RuntimeException("권한이 없습니다.");
            }
        } else {
            throw new RuntimeException("존재하지 않는 사용자");
        }
    }

    public void delete(User user) {

        Optional<User> findUser = ur.findById(user.getId());
        if (findUser.isPresent()) {
            User u = findUser.get();
            if (user.getPassword().equals(user.getConfirmPassword())) {
                if (encoder.matches(user.getPassword(), u.getPassword())) {
                    u.setDeleted(true); // 논리적 삭제. db에서는 삭제 안 되지만, 애플리케이션 상에서는 삭제된 것처럼 보임
                    ur.save(u);
                } else {
                    throw new RuntimeException("db 정보 불일치");
                }
            } else {
                throw new RuntimeException("두 비밀번호 불일치");
            }
        } else {
            throw new RuntimeException("존재하지 않는 회원");
        }
    }
}
