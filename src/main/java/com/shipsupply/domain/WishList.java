package com.shipsupply.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WishList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    private String item;

    private String category;

    private String machinery;

    private String currency;

    private Long price;

    @Setter
    private String company;

    private Long leadtime;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;
    
    // 동일성 검증 해야됨
}
