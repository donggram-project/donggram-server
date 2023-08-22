package com.donggram.back.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
public class MemberDetailsDto {

    private Long memberId;
    private String studentId;
    private String memberName;
    private String college1;
    private String major1;
    private String college2;
    private String major2;
    private String profileImage;
    private String role;
    private List<String> clubList;

}
