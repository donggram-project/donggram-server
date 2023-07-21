package com.donggram.back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class CollegeDto {

    private final Long college_id;
    private final String college_name;
}
