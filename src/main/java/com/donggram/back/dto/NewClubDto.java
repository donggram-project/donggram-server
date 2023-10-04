package com.donggram.back.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewClubDto {

    private String college;
    private String division;
    private String clubName;
    private String content;
    private String recruitment_period;
    private boolean isRecruitment;

}
