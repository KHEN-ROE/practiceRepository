package com.shipsupply.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @Column(name = "userId")
    @Setter
    private String id;
    @Setter
    private String password;
    @Setter
    private String newPassword;

    private String confirmPassword;

    @Setter
    private String email;
    @Setter
    private String username;
    @Setter
    private String role;

    //소셜 로그인 서비스를 제공하는 업체(구글, 페이스북, 깃허브 등)
    @Setter
    private String provider; // 이걸 다른 테이블로 만들어야할 듯
    // 사용자의 소셜로그인 아이디
    @Setter
    private String providerId;

    @Setter
    private boolean deleted = false; // 유저 논리적 삭제(삭제된 것 처럼 보이지만 db에서 삭제X)를 위한 필드
                                     // 반대는 물리적 삭제(db에서 실제로 삭제)

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof User user)) return false;
//        return Objects.equals(getId(), user.getId());
//    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
