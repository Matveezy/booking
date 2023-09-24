package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Embeddable
@Table(name = "HOTEL")
@NoArgsConstructor
public class Order {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "USERID")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "HOTELID")
    private Hotel hotel;

    @Column(name = "DATEIN", nullable = false)
    private Instant dateIn;

    @Column(name = "DATEOUT", nullable = false)
    private Instant dateOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROOMCLASS", nullable = false)
    private RoomClass roomClass;
}
