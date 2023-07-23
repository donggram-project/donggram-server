package com.donggram.back.service;

import com.donggram.back.dto.CollegeDto;
import com.donggram.back.dto.DivisionDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.entity.College;
import com.donggram.back.entity.Division;
import com.donggram.back.repository.CollegeRepository;
import com.donggram.back.repository.DivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionService {
    private final DivisionRepository divisionRepository;

    public ResponseDto getAllDivision(){

        List<Division> divisionList = divisionRepository.findAll();

        ArrayList<DivisionDto> dtoArrayList = new ArrayList<>();
        for (Division i : divisionList){

            dtoArrayList.add(DivisionDto.builder()
                    .divisionId(i.getId())
                    .divisionName(i.getName())
                    .build());
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("분과 카테고리")
                .data(dtoArrayList)
                .build();
    }
}
