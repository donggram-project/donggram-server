package com.donggram.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLUB_ID")
    private long id;

    @Column(name = "CLUB_NAME")
    private String clubName;

    @Column(name = "CLUB_CREATED")
    private String clubCreated;

    @Column(name = "CLUB_CONTENT")
    private String content;

    @Column(name = "CLUB_RECRUITMENT")
    private boolean isRecruitment;

    @Column(name = "CLUB_RECRUITMENT_PERIOD")
    private String recruitment_period;

    //일대다, 양방향
    @JsonIgnore
    @OneToMany(mappedBy = "club")
    private List<ClubJoin> clubJoinList = new ArrayList<>();

    // 다대일, 단방향
    @ManyToOne
    @JoinColumn(name = "COLLEGE_ID")
    private College college;

    @ManyToOne
    @JoinColumn(name = "DIVISION_ID")
    private Division division;

}
