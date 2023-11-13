package com.donggram.back.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ImageProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
