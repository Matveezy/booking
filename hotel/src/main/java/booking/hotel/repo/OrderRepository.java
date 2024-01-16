package booking.hotel.repo;

import booking.hotel.entity.Hotel;
import booking.hotel.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserId(long userId);
    List<Order> findOrdersByRoomId(long roomId);
}
