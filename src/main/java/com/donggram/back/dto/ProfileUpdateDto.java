package com.donggram.back.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDto {
    private String studentId;
    private String memberName;
    private String college1;
    private String major1;
    private String college2;
    private String major2;
    private String role;
    private MultipartFile profileImage;

}
