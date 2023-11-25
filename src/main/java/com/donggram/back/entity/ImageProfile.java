package com.donggram.back.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ImageProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ImageProfile (String url, Member member){
        this.url = url;
        this.member = member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void uploadBasicImage(){
        this.url = "https://image-profile-bucket.s3.ap-northeast-2.amazonaws.com/basic_profile.png";
    }
}
