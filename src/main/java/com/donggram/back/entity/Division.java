package com.donggram.back.entity;

import javax.persistence.*;

@Entity
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DIVISION_ID")
    private Long id;

    @Column(name = "DIVISION_NAME")
    private String name;
}
