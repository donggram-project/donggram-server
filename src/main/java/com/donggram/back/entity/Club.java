package com.donggram.back.entity;

import com.donggram.back.dto.ClubDetailsDto;
import com.donggram.back.dto.ClubProfileUpdateDto;
import com.donggram.back.dto.ProfileUpdateDto;
import com.donggram.back.repository.CollegeRepository;
import com.donggram.back.repository.DivisionRepository;
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

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "image_club_id")
    private ImageClub imageClub;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "clubRequest_id")
    private ClubRequest clubRequest;


    //일대다, 양방향
    @JsonIgnore
    @OneToMany(mappedBy = "club", orphanRemoval = true)
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

    public void updateClubProfile(ClubProfileUpdateDto clubProfileUpdateDto, CollegeRepository collegeRepository, DivisionRepository divisionRepository) {
        this.clubName = clubProfileUpdateDto.getClubName();

        if(clubProfileUpdateDto.getCollege() != null){
            College college = collegeRepository.findByName(clubProfileUpdateDto.getCollege()).orElseThrow(() -> new RuntimeException("해당 단과대가 존재하지 않습니다."));
            this.college = college;
        }
        if(clubProfileUpdateDto.getDivision() != null){
            Division division = divisionRepository.findByName(clubProfileUpdateDto.getDivision()).orElseThrow(() -> new RuntimeException("해당 분과가 존재하지 않습니다."));
            this.division =division;
        }
        this.content = clubProfileUpdateDto.getContent();
        this.clubName = clubProfileUpdateDto.getClubName();
        this.clubCreated = clubProfileUpdateDto.getClubCreated();
        this.isRecruitment = clubProfileUpdateDto.isRecruitment();
        this.recruitment_period = clubProfileUpdateDto.getRecruitmentPeriod();

    }
}
