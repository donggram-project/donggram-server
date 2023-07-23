package com.donggram.back.service;

import com.donggram.back.dto.ClubDetailsDto;
import com.donggram.back.dto.ClubDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.entity.Club;
import com.donggram.back.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<ClubDetailsDto> clubDetailsDtoList = new ArrayList<>();
        for(Long id : clubIds){
            List<Club> clubsByCollegeId = clubRepository.findClubsByCollegeId(id);
            // 중복 여부 체크
            for(Club club : clubsByCollegeId){
                ClubDetailsDto clubDetailsDto = ClubDetailsDto.builder()
                        .clubId(club.getId())
                        .clubName(club.getClubName())
                        .college(club.getCollege().getName())
                        .division(club.getDivision().getName())
                        .isRecruitment(club.isRecruitment())
                        .build();
                if (clubDetailsDtoList.contains(clubDetailsDto)){
                    break;
                }else {
                    clubDetailsDtoList.add(clubDetailsDto);
                }
            }
        }
        return ResponseDto.builder()
                .status(200)
                .responseMessage("선택된 동아리 정보 API")
                .data(clubDetailsDtoList)
                .build();
    }

    public ResponseDto getClubDetails(Long clubId){
        Optional<Club> clubOptional = clubRepository.findById(clubId);

        if(clubOptional.isPresent()){
            Club club = clubOptional.get();
            ClubDetailsDto clubDetailsDto = ClubDetailsDto.builder()
                    .clubId(club.getId())
                    .clubName(club.getClubName())
                    .recruitmentPeriod(club.getRecruitment_period())
                    .isRecruitment(club.isRecruitment())
                    .college(club.getCollege().getName())
                    .division(club.getDivision().getName())
                    .content(club.getContent())
                    .clubCreated(club.getClubCreated())
                    .build();

            return ResponseDto.builder()
                    .status(200)
                    .responseMessage("동아리 세부정보 API")
                    .data(clubDetailsDto)
                    .build();

        }else {
            return ResponseDto.builder()
                    .status(400)
                    .responseMessage("해당 동아리 정보 NULL")
                    .data("NULL")
                    .build();
        }
    }
}
