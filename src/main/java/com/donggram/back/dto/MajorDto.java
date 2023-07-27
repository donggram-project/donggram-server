package com.donggram.back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class MajorDto {
    private final Long majorId;
    private final String majorName;
}
