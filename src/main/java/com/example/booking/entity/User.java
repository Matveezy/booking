package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "USER")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @Column(name = "PASS", nullable = false)
    private char[] pass;

    @Column(name = "DATE0FBIRTH", nullable = false)
    private Instant dateOfBirth;

    @OneToMany(mappedBy = "owner")
    private List<Hotel> ownedHotels;

}
