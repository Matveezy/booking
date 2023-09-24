package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "GUESTINFO")
@NoArgsConstructor
public class GuestInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @Column(name = "DATEOFBIRTH", nullable = false)
    private Long dateOfBirth;

    @Column(name = "PASSPORT", nullable = false)
    private String passport;
}
