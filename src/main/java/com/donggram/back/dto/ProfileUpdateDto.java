package com.donggram.back.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProfileUpdateDto {
    private String studentId;
    private String memberName;
    private String college1;
    private String major1;
    private String college2;
    private String major2;
    private MultipartFile profileImage;

}
