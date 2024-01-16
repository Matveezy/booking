package booking.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "hotelclass")
    private HotelClass hotelClass;

    @ElementCollection
    @CollectionTable(
            name = "hotelowner",
            joinColumns = @JoinColumn(name = "hotelid")
    )
    @Column(name = "userid")
    private List<Long> userId;

    @Builder.Default
    @OneToMany(mappedBy = "hotel")
    private List<Order> orders = new ArrayList<>();
}
