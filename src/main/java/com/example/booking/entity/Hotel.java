package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@Builder
@Table(name = "HOTEL")
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "HOTELCLASS", nullable = false)
    private HotelClass hotelClass;

    @ManyToOne
    @JoinTable(
            name = "HOTELOWNER",
            joinColumns = @JoinColumn(name = "HOTELID"),
            inverseJoinColumns = @JoinColumn(name = "USERID")
    )
    private User owner;

}
