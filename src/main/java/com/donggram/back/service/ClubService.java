package com.donggram.back.service;

import com.donggram.back.dto.ClubDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.entity.Club;
import com.donggram.back.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    List<ClubDto> clubDtoList = new ArrayList<>();
    //모든 동아리 정보 가져오기
    public ResponseDto getAllClubs(){
        for (Club club : clubRepository.findAll()) {
            clubDtoList.add(ClubDto.builder()
                            .clubId(club.getId())
                    .clubName(club.getClubName())
                    .college(club.getCollege().getName())
                    .division(club.getDivision().getName())
                    .isRecruitment(club.isRecruitment())
                    .build());
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("모든 동아리 정보 API")
                .data(clubDtoList)
                .build();
    }

    public ResponseDto getSelectedClubs(List<Long> clubIds){

        List<ClubDto> clubDtoList = new ArrayList<>();
        for(Long id : clubIds){
            List<Club> clubsByCollegeId = clubRepository.findClubsByCollegeId(id);
            // 중복 여부 체크
            for(Club club : clubsByCollegeId){
                ClubDto clubDto = ClubDto.builder()
                        .clubId(club.getId())
                        .clubName(club.getClubName())
                        .college(club.getCollege().getName())
                        .division(club.getDivision().getName())
                        .isRecruitment(club.isRecruitment())
                        .build();
                if (clubDtoList.contains(clubDto)){
                    break;
                }else {
                    clubDtoList.add(clubDto);
                }
            }
        }
        return ResponseDto.builder()
                .status(200)
                .responseMessage("선택된 동아리 정보 API")
                .data(clubDtoList)
                .build();
    }
}
