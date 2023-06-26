package com.shipsupply.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Hit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hit;

    @ManyToOne @JoinColumn(name = "userId")
    private User user;

    @ManyToOne @JoinColumn(name = "boardId")
    @JsonIgnore
    private Board board;

    @ManyToOne @JoinColumn(name = "commentId")
//    @JsonIgnore
    private Comment comment;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Hit hit)) return false;
//        return Objects.equals(getHit(), hit.getHit());
//    }

    @Override
    public int hashCode() {
        return Objects.hash(getHit());
    }

}
