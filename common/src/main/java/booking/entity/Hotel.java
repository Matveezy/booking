package booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "HOTEL")
@ToString(exclude = {"orders"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "HOTELCLASS", nullable = false)
    private HotelClass hotelClass;

    @Builder.Default
    @OneToMany(mappedBy = "hotel")
    private List<Order> orders = new ArrayList<>();

    @ManyToOne
    @JoinTable(
            name = "HOTELOWNER",
            joinColumns = @JoinColumn(name = "HOTELID"),
            inverseJoinColumns = @JoinColumn(name = "USERID")
    )
    private User owner;

}