package com.donggram.back.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAJOR_ID")
    private Long id;

    @Column(name = "MAJOR_NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "COLLEGE_ID")
    private College college;
}