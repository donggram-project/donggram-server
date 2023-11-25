package com.donggram.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubProfileUpdateDto {

    private Long clubId;
    private String college;
    private String division;
    private String clubName;
    private String clubCreated;
    private String content;
    private String recruitmentPeriod;
    private boolean isRecruitment;
    private String writer;
}
