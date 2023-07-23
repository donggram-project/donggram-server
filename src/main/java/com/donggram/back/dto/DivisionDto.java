package com.donggram.back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class DivisionDto {

    private final Long divisionId;
    private final String divisionName;
}