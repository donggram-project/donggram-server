package com.donggram.back.entity;

import com.donggram.back.dto.ClubDetailsDto;
import com.donggram.back.dto.ClubProfileUpdateDto;
import com.donggram.back.dto.ProfileUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageClub_id")
    private ImageClub imageClub;

    //일대다, 양방향
    @JsonIgnore
    @OneToMany(mappedBy = "club")
    private List<ClubJoin> clubJoinList = new ArrayList<>();

    // 다대일, 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLLEGE_ID")
    private College college;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIVISION_ID")
    private Division division;

    public void updateClubJoinList(ClubJoin clubJoin){
        this.clubJoinList.add(clubJoin);
    }

    public void setImageClub(ImageClub imageClub) {
        this.imageClub = imageClub;
    }

    public void updateClubProfile(ClubProfileUpdateDto clubProfileUpdateDto) {
        this.clubName = clubProfileUpdateDto.getClubName();
//        this.college = clubProfileUpdateDto.getCollege();
//        this.division = clubProfileUpdateDto.getDivision();
//
//
//
//        // 역할 정보를 업데이트
//        if (profileUpdateDto.getRole() != null) {
//            this.roles.clear(); // 기존 역할 정보 모두 삭제
//            this.roles.addAll(Collections.singleton(profileUpdateDto.getRole())); // 새로운 역할 정보 추가
//        }
    }
}
