package com.donggram.back.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DIVISION_ID")
    private Long id;

    @Column(name = "DIVISION_NAME")
    private String name;
}
