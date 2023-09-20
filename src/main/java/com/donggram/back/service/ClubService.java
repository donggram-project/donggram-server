package com.donggram.back.service;

import com.donggram.back.dto.ClubDetailsDto;
import com.donggram.back.dto.ClubDto;
import com.donggram.back.dto.NewClubDto;
import com.donggram.back.dto.ResponseDto;
import com.donggram.back.entity.*;
import com.donggram.back.jwt.JwtTokenProvider;
import com.donggram.back.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubJoinRepository clubJoinRepository;
    private final MemberRepository memberRepository;
    private final CollegeRepository collegeRepository;
    private final DivisionRepository divisionRepository;


    List<ClubDto> clubDtoList = new ArrayList<>();
    //모든 동아리 정보 가져오기
    @Transactional
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

    @Transactional
    public ResponseDto findClubsByFilters(String keyword, List<Long> collegeIds, List<Long> divisionIds, Pageable pageable){
        List<Club> clubsByFilters = clubRepository.findClubsByFilters(keyword, collegeIds, divisionIds, pageable).getContent();
        if (!clubsByFilters.isEmpty()){
            System.out.println(clubsByFilters.get(0));
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("모든 동아리 정보 API")
                .data(clubsByFilters)
                .build();
    }

    @Transactional
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

    public ResponseDto postClubJoin(Long clubId, String studentId){

        // ClubJoin Entity를 만들어야 함
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        Member member = memberRepository.findByStudentId(studentId).get();

        if(clubOptional.isPresent()){
            Club club = clubOptional.get();
            ClubJoin clubJoin = ClubJoin.builder()
                    .role(Role.MEMBER)
                    .joinDate(LocalDate.now())
                    .member(member)
                    .club(club)
                    .build();

            clubJoinRepository.save(clubJoin);

            return ResponseDto.builder()
                    .status(200)
                    .responseMessage("동아리 가입신청 완료")
                    .data("null")
                    .build();

        }else {
            return ResponseDto.builder()
                    .status(400)
                    .responseMessage("동아리 정보 없음")
                    .data("NULL")
                    .build();
        }

    }

    @Transactional
    public ResponseDto postNewClub(NewClubDto newClubDto){

        Optional<College> byCollegeName = collegeRepository.findByName(newClubDto.getCollege());
        Optional<Division> byDivisionName = divisionRepository.findByName(newClubDto.getDivision());

        if(byCollegeName.isEmpty()){
            throw new RuntimeException("해당 단과대 이름이 존재하지 않습니다.");
        }
        if(byDivisionName.isEmpty()){
                throw new RuntimeException("해당 분과 이름이 존재하지 않습니다.");
        }
        College college = byCollegeName.get();
        Division division = byDivisionName.get();

        //동아리 중복 확인
        String clubName = newClubDto.getClubName();
        Optional<Club> byClubName = clubRepository.findByClubName(clubName);
        if(byClubName.isPresent()){
            throw new RuntimeException("이미 존재하는 동아리 입니다.");
        }else{
            Club club = Club.builder()
                    .college(college)
                    .division(division)
                    .clubName(newClubDto.getClubName())
                    .content(newClubDto.getContent())
                    .isRecruitment(newClubDto.isRecruitment())
                    .clubCreated(LocalDateTimeToString())
                    .recruitment_period(newClubDto.getRecuritmentPeriod())
                    .build();

            clubRepository.save(club);

            return ResponseDto.builder()
                    .status(200)
                    .responseMessage("동아리 생성 완료")
                    .data(club)
                    .build();
        }


    }


    public String LocalDateTimeToString(){
        // 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // LocalDateTime을 문자열로 변환
        String formattedDate = now.format(formatter);

        return formattedDate;
    }


}
