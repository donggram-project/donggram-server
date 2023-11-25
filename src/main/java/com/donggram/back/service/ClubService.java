package com.donggram.back.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.donggram.back.dto.*;
import com.donggram.back.entity.*;
import com.donggram.back.jwt.JwtTokenProvider;
import com.donggram.back.repository.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final ClubRequestRepository clubRequestRepository;
    private final AmazonS3Client amazonS3Client;
    private final ImageClubRepository imageClubRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    //모든 동아리 정보 가져오기
    @Transactional
    public ResponseDto getAllClubs(){
        List<ClubDto> clubDtoList = new ArrayList<>();
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
    public ResponseDto getSelectedClubs(List<Long> collegeIds, List<Long> divisionIds){
        List<Club> clubs = new ArrayList<>();
        List<ClubDto> clubDtos = new ArrayList<>();
        for(int i = 0; i < collegeIds.size(); i++){
            for (int j = 0; j < divisionIds.size(); j++) {
                List<Club> clubsByCollegeAndDivision = clubRepository.findClubsByCollegeAndDivision(collegeIds.get(i), divisionIds.get(j));
                for(Club club : clubsByCollegeAndDivision){
                    clubs.add(club);
                }
            }
        }

        for (Club club : clubs) {
            clubDtos.add(ClubDto.builder()
                    .clubId(club.getId())
                    .clubName(club.getClubName())
                    .college(club.getCollege().getName())
                    .division(club.getDivision().getName())
                    .isRecruitment(club.isRecruitment())
                    .build());
        }

        return ResponseDto.builder()
                .status(200)
                .responseMessage("해당되는 동아리 정보 API")
                .data(clubDtos)
                .build();

    }

    @Transactional
    public ResponseDto findByKeyword(String keyword){
        List<ClubDto> clubDtoList = new ArrayList<>();
        List<Club> clubsByFilters = clubRepository.searchClubs("%" + keyword + "%");
        if (!clubsByFilters.isEmpty()){
            for (Club club : clubsByFilters) {
                clubDtoList.add(ClubDto.builder()
                        .clubId(club.getId())
                        .clubName(club.getClubName())
                        .college(club.getCollege().getName())
                        .division(club.getDivision().getName())
                        .isRecruitment(club.isRecruitment())
                        .build());
            }

        }
        return ResponseDto.builder()
                .status(200)
                .responseMessage("검색 결과 조회 API")
                .data(clubDtoList)
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

    @Transactional
    public ResponseDto approveMember(Long memberId, Long clubRequestId){


        ClubRequest clubRequest = clubRequestRepository.findById(clubRequestId)
                .orElseThrow(() -> new RuntimeException("해당 동아리 요청 엔티티가 존재하지 않습니다."));

        long clubId = clubRequest.getClub().getId();

        ClubJoin clubJoin = clubJoinRepository.findByMemberIdAndClubId(memberId, clubId)
                .orElseThrow(() -> new RuntimeException("해당 멤버의 동아리 가입신청이 존재하지 않습니다."));

        // 가입신청 상태 변경
        clubJoin.updateStatus(RequestStatus.approve);

        return ResponseDto.builder()
                .status(200)
                .responseMessage("동아리 가입신청 승인 완료")
                .data("NULL").build();
    }

    @Transactional
    public ResponseDto memberReject(Long memberId, Long clubRequestId){


        ClubRequest clubRequest = clubRequestRepository.findById(clubRequestId)
                .orElseThrow(() -> new RuntimeException("해당 동아리 요청 엔티티가 존재하지 않습니다."));

        long clubId = clubRequest.getClub().getId();

        ClubJoin clubJoin = clubJoinRepository.findByMemberIdAndClubId(memberId, clubId)
                .orElseThrow(() -> new RuntimeException("해당 멤버의 동아리 가입신청이 존재하지 않습니다."));

        // 가입신청 상태 변경
        clubJoin.updateStatus(RequestStatus.rejected);

        return ResponseDto.builder()
                .status(200)
                .responseMessage("동아리 가입신청 거절 완료")
                .data("NULL").build();
    }

    public ResponseDto postClubJoin(Long clubId, String studentId){
        Optional<Club> clubOptional = clubRepository.findById(clubId);
        Member member = memberRepository.findByStudentId(studentId).get();

        if(clubOptional.isPresent()){
            Club club = clubOptional.get();

            // Check if the ClubJoin already exists
            Optional<ClubJoin> clubJoinOptional = clubJoinRepository.findByMemberAndClub(member, club);

            if (clubJoinOptional.isPresent()) {
                return ResponseDto.builder()
                        .status(410)
                        .responseMessage("이미 가입신청이 되어있습니다.")
                        .data("NULL")
                        .build();
            }

            ClubJoin clubJoin = ClubJoin.builder()
                    .role(Role.MEMBER)
                    .joinDate(LocalDateTimeToString())
                    .status(RequestStatus.pending)
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
    @JsonIgnore
    public ResponseDto postNewClub(NewClubDto newClubDto, MultipartFile multipartFile, String token){

        String studentId = jwtTokenProvider.getUserPk(token);
        Member member = memberRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("해당 학번이 존재하지 않습니다."));



        //동아리 중복 확인
        String clubName = newClubDto.getClubName();
        Optional<Club> byClubName = clubRepository.findByClubName(clubName);

        if(byClubName.isPresent()){
            throw new RuntimeException("이미 존재하는 동아리 입니다.");
        }else{



           ImageClub imageClub = new ImageClub();

           ClubRequest clubRequest = ClubRequest.builder()
                    .college(newClubDto.getCollege())
                    .division(newClubDto.getDivision())
                    .clubName(newClubDto.getClubName())
                    .content(newClubDto.getContent())
                    .club_created(LocalDateTimeToString())
                    .isRecruitment(newClubDto.isRecruitment())
                    .recruitment_period(newClubDto.getRecruitment_period())
                    .status(RequestStatus.pending)
                    .member(member)
                    .build();

            if (multipartFile.isEmpty()){
                imageClub.uploadBasicImage();
            }else {

                String s = uploadImage(multipartFile, clubRequest);
                imageClub.uploadCustomImage(s);
            }


            clubRequest.setImageClub(imageClub);
            imageClub.setClubRequest(clubRequest);
            imageClubRepository.save(imageClub);

            clubRequestRepository.save(clubRequest);

            ClubRequestDto build = ClubRequestDto.builder()
                    .college(clubRequest.getCollege())
                    .division(clubRequest.getDivision())
                    .clubName(clubRequest.getClubName())
                    .imageClub(clubRequest.getImageClub().getUrl())
                    .club_created(clubRequest.getClub_created())
                    .isRecruitment(clubRequest.isRecruitment())
                    .recruitment_period(clubRequest.getRecruitment_period())
                    .status(clubRequest.getStatus().toString())
                    .content(clubRequest.getContent())
                    .build();

            return ResponseDto.builder()
                    .status(200)
                    .responseMessage("동아리 생성 요청 완료")
                    .data(build)
                    .build();
        }
    }



    public static String LocalDateTimeToString(){
        // 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // LocalDateTime을 문자열로 변환
        String formattedDate = now.format(formatter);

        return formattedDate;
    }

    public String uploadImage(MultipartFile file, ClubRequest clubRequest) {
        try {
            String imageFileName = "club_" + clubRequest.getId() + "_" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, imageFileName, file.getInputStream(), metadata);

            String imageUrl = "https://image-profile-bucket.s3.ap-northeast-2.amazonaws.com/" + imageFileName;

            return imageUrl;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
