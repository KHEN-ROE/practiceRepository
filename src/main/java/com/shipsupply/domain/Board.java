package com.shipsupply.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardId")
    private Long id; //seq 말고 id 쓰는게 관례

    @Setter
    private String title;

    @Setter
    @Column(length = 1000) // 텍스트 길이 1000으로 설정
    private String text;

    @Setter
    private Date date;

    @Setter
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Setter
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Hit> hits;

    @Setter
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    // 동일성 검증
//    @Override
//    public boolean equals(Object o) { // 객체의 동일성 검증하는 데 활용
//        if (this == o) return true; // 현재객체(this)와 비교대상 객체(o)가 메모리 상에서 같은 객체 가리키는지 확인
//        if (!(o instanceof Board board)) return false; // o가 Board타입의 객체인지 확인
//        return Objects.equals(getId(), board.getId()); //this의 seq와 Board타입 객체의 getSeq가 동일한지 검증
//    }// Objects.equals() 메서드는 두 객체가 null인지 확인하는 추가적인 처리를 하므로, NullPointerException을 방지하는데 유용

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Board)) return false;
//        Board board = (Board) o;
//        return Objects.equals(getId(), board.getId());
//    }


    // 일반적으로 equals 메서드를 오버라이딩하는 경우, hashCode 메서드도 같이 오버라이딩함
    // why? equals()에서 두 객체가 같으면 해시코드도  같아야하기 때문.
    @Override
    public int hashCode() {
        return Objects.hash(getId()); // 해시코드 값 반환
    }
}
