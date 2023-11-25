package com.donggram.back.entity;

import com.donggram.back.dto.ProfileUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEBER_ID")
    private long id;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String college1;

    @Column
    private String college2;

    @Column(nullable = false)
    String major1;

    @Column
    String major2;

    @OneToOne(mappedBy = "member", orphanRemoval = true)
    ImageProfile imageProfile;

    @OneToOne
    private ClubRequest clubRequest;

    // 일대다, 양방향
    @JsonIgnore
    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private final List<ClubJoin> clubJoinList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public void setImageProfile(ImageProfile imageProfile) {
        this.imageProfile = imageProfile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }



    public void updateProfile(ProfileUpdateDto profileUpdateDto) {
        this.name = profileUpdateDto.getMemberName();
        this.studentId = profileUpdateDto.getStudentId();
        this.major1 = profileUpdateDto.getMajor1();
        this.major2 = profileUpdateDto.getMajor2();
//        this.imageProfile = profileUpdateDto.getProfileImage();

        // 역할 정보를 업데이트
        if (profileUpdateDto.getRole() != null) {
            this.roles.clear(); // 기존 역할 정보 모두 삭제
            this.roles.addAll(Collections.singleton(profileUpdateDto.getRole())); // 새로운 역할 정보 추가
        }
    }
    public void updateImageProfile(ImageProfile imageProfile){
        this.imageProfile = imageProfile;
    }

}
