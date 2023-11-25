package com.donggram.back.dto;

import lombok.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ClubRequestDto {

    private String college;
    private String division;
    private String clubName;
    private String content;
    private String imageClub;
    private String recruitment_period;
    private boolean isRecruitment;
    private String club_created;
    private String status;

}
