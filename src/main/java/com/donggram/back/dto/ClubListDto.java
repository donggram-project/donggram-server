package com.donggram.back.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClubListDto {

    private Long id;
    private String club_name;
    private String student_name;
    private String major;
    private String apply_date;
    private String status;

}
