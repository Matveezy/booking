package booking.common.repository;

import booking.common.entity.Room;
import booking.common.entity.RoomClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends PagingAndSortingRepository<Room, Long>, CrudRepository<Room, Long> {
    Optional<Room> findRoomById(long id);
    List<Room> findRoomsByHotel_Id(long id, Pageable pageable);
    List<Room> findRoomsByRoomClassAndHotel_Id(RoomClass roomClass, long id, Pageable pageable);
}
