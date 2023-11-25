package com.donggram.back.dto;

import com.donggram.back.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ClubMemberDto {

    private Long id;
    private String name;
    private String division;
    private String join_dated;
    private RequestStatus status;
}
