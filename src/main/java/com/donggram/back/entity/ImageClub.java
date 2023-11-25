package com.donggram.back.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ImageClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "clubRequest_id")
    private ClubRequest clubRequest;

    @OneToOne(mappedBy = "imageClub")
    private Club club;

    @Builder
    public ImageClub (String url, ClubRequest clubRequest){
        this.url = url;
        this.clubRequest = clubRequest;
    }

    public void setClubRequest(ClubRequest clubRequest) {
        this.clubRequest = clubRequest;
    }

    public void uploadBasicImage(){
        this.url = "https://image-profile-bucket.s3.ap-northeast-2.amazonaws.com/basic_profile.png";
    }

    public void uploadCustomImage(String image){
        this.url = image;
    }
}
