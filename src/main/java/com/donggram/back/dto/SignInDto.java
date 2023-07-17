package com.donggram.back.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class SignInDto {

    @NotBlank(message = "학번을 입력해주세요. ")
    private Long studentId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
