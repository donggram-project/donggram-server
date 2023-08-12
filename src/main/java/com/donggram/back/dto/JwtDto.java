package com.donggram.back.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;

@Getter
@RequiredArgsConstructor
public class JwtDto {

    private final String jwt;

}
