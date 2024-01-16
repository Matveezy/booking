package booking.hotel.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hotelid")
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(name = "roomclass")
    private RoomClass roomClass;

    @Column(name = "roomnumber")
    private Long roomNumber;

    @Column
    private Long price;

    @Builder.Default
    @OneToMany(mappedBy = "room")
    private List<Order> ordersThisRoom = new ArrayList<>();
}
