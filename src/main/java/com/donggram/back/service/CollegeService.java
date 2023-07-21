package com.donggram.back.service;

import com.donggram.back.dto.CollegeDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.entity.College;
import com.donggram.back.repository.CollegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollegeService {
    private final CollegeRepository collegeRepository;

    public ResponseDto getAllCollege(){

        List<College> collegeList = collegeRepository.findAll();

        ArrayList<CollegeDto> dtoArrayList = new ArrayList<>();
        for (College i : collegeList){

            dtoArrayList.add(CollegeDto.builder()
                    .college_id(i.getId())
                    .college_name(i.getName())
                    .build());
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("단과대 카테고리")
                .data(dtoArrayList)
                .build();
    }
}
