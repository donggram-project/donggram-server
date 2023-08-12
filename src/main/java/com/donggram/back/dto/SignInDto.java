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
public class SignInDto {

    @NotBlank(message = "학번을 입력해주세요. ")
    private String studentId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
