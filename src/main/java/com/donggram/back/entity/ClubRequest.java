package com.donggram.back.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COLLEGE_NAME")
    private String college;

    @Column(name = "DIVISION_NAME")
    private String division;

    @Column(name = "CLUB_NAME")
    private String clubName;

    @Column(name = "CLUB_CONTENT")
    private String content;

    @Column(name = "CLUB_RECRUITMENT_PERIOD")
    private String recruitment_period;

    @Column(name = "CLUB_RECRUITMENT")
    private boolean isRecruitment;

    @Column(name = "CLUB_CREATED")
    private String club_created;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateStatus(RequestStatus status){
        this.status = status;
    }

}
