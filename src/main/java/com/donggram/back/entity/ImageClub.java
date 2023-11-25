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
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToOne
    @JoinColumn(name = "clubRequest_id")
    private ClubRequest clubRequest;

    @Builder
    public ImageClub (String url, Club club){
        this.url = url;
        this.club = club;
    }
}
