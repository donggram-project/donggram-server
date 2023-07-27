package com.donggram.back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class CollegeDto {

    private final Long collegeId;
    private final String collegeName;
}
