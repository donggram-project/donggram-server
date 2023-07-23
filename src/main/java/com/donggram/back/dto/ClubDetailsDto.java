package com.donggram.back.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ClubDetailsDto {

    private final Long clubId;
    private final String college;
    private final String division;
    private final String clubName;
    private final String clubCreated;
    private final String content;
    private final String recruitmentPeriod;
    private final List<String> clubList;
    private final boolean isRecruitment;

}
