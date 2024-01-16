package booking.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Hotel hotel;

    @Column(nullable = false)
    private Instant dateIn;

    @Column(nullable = false)
    private Instant dateOut;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Room room;

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        hotel.getOrders().add(this);
    }

    public void setRoom(Room room) {
        this.room = room;
        room.getOrdersThisRoom().add(this);
    }
}
