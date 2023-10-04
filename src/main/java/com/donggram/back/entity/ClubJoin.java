package com.donggram.back.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubJoin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLUBJOIN_ID")
    private Long id;

    //생성일
    private String joinDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 다대일, 양방향
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID")
    private Club club;


}
