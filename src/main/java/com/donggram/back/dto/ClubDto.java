package com.donggram.back.dto;

import lombok.*;

@Getter
@RequiredArgsConstructor
@Builder
public class ClubDto {

    private final Long clubId;
    private final String college;
    private final String division;
    private final String clubName;
    private final String content;
    private final String RecuritPeriod;
    private final boolean isRecruitment;
    private final String clubImage;
}
