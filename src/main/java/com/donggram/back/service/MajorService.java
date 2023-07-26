package com.donggram.back.service;

import com.donggram.back.dto.MajorDto;
import com.donggram.back.entity.Major;
import com.donggram.back.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import com.donggram.back.dto.ResponseDto;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;


    public ResponseDto getAllMajor() {

        List<Major> majorList = majorRepository.findAll();

        ArrayList<MajorDto> dtoArrayList = new ArrayList<>();
        for(Major i : majorList) {

            dtoArrayList.add(MajorDto.builder()
                    .majorId(i.getId())
                    .majorName(i.getName())
                    .build());
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("전공 카테고리")
                .data(dtoArrayList)
                .build();
    }
}
