package com.donggram.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SignUpDto {

    @NotBlank(message = "학번을 입력해주세요.")
    private String studentId;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    private String checkPassword;

    @NotBlank(message = "단과대를 선택해주세요.")
    private String college1;

    private String college2;

    @NotBlank(message = "전공을 선택해주세요.")
    private String major1;

    private String major2;

    private String role;

}
