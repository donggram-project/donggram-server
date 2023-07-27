package com.donggram.back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ClubDto {

    private final Long clubId;
    private final String college;
    private final String division;
    private final String clubName;
    private final boolean isRecruitment;
}
