package booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ROOM")
@ToString(exclude = {"ordersThisRoom"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "HOTELID")
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROOMCLASS", nullable = false)
    private RoomClass roomClass;

    @Column(name = "ROOMNUMBER", nullable = false)
    private Long roomNumber;

    @Column(name = "PRICE", nullable = false)
    private Long price;

    @Builder.Default
    @OneToMany(mappedBy = "room")
    private List<Order> ordersThisRoom = new ArrayList<>();
}
