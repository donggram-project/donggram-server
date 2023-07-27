package com.donggram.back.dto;

import com.donggram.back.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberListDto {

    private Long id;
    private String name;
    private String studentId;
    private String major;
    private String role;

}
