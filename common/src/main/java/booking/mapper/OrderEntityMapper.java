package booking.mapper;

import booking.dto.CreateOrderDto;
import booking.entity.Order;
import booking.entity.Room;
import booking.entity.User;
import booking.exception.EntityNotFoundException;
import booking.repository.RoomRepository;
import booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderEntityMapper implements Mapper<CreateOrderDto, Order> {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public Order map(CreateOrderDto createOrderDto) {
        Optional<User> maybeUser = userRepository.findUserById(createOrderDto.getUserId());
        if (maybeUser.isEmpty())
            throw new EntityNotFoundException("User with id: " + createOrderDto.getUserId() + " not found!");
        Optional<Room> maybeRoom = roomRepository.findRoomById(createOrderDto.getRoomId());
        if (maybeRoom.isEmpty())
            throw new EntityNotFoundException("Room with id + " + createOrderDto.getRoomId() + "not found!");
        return Order.builder()
                .user(maybeUser.get())
                .hotel(maybeRoom.get().getHotel())
                .room(maybeRoom.get())
                .dateIn(createOrderDto.getDateIn())
                .dateOut(createOrderDto.getDateOut())
                .createdAt(Instant.now())
                .build();
    }
}
