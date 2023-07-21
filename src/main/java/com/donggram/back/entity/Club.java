package com.donggram.back.entity;

import javax.annotation.processing.Generated;
import javax.persistence.*;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "CLUB_NAME")
    private String clubName;

    @Column(name = "POST_CREATED")
    private String postCreated;

    @Column(name = "POST_CONTENT")
    private String content;

    @Column(name = "CLUB_RECRUITMENT")
    private boolean isRecruitment;

    // 다대일, 단방향
    @ManyToOne
    @JoinColumn(name = "DIVISION_ID")
    private Division division;






}
