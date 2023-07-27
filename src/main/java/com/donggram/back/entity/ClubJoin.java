package com.donggram.back.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class ClubJoin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLUBJOIN_ID")
    private Long id;

    private LocalDate joinDate;

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
