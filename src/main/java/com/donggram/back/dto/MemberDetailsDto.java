package com.donggram.back.dto;


import com.donggram.back.entity.ClubJoin;
import com.donggram.back.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
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

    public MemberDetailsDto(Member member) {
        this.memberId = member.getId();
        this.studentId = member.getStudentId();
        this.memberName = member.getName();
        this.college1 = member.getCollege1();
        this.major1 = member.getMajor1();
        this.college2 = member.getCollege2();
        this.major2 = member.getMajor2();
        this.profileImage = member.getProfileImage();

        List<String> clubList = new ArrayList<>();
        for (ClubJoin clubJoin: member.getClubJoinList()) {
            String clubName = clubJoin.getClub().getClubName();
            clubList.add(clubName);
        }
        this.clubList = clubList;
    }

}
