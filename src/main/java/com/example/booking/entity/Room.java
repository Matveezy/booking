package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ROOM")
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "HOTELID")
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROOMCLASS", nullable = false)
    private RoomClass roomClass;

    @Column(name = "MAXGUESTSCOUNT", nullable = false)
    private Long maxGuestsCount;

    @Column(name = "ROOMNUMBER", nullable = false)
    private Long roomNumber;

    @Column(name = "FLOOR", nullable = false)
    private Long floor;
}
