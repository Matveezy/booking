package booking.common.mapper;

import booking.common.repository.RoomRepository;
import booking.common.repository.UserRepository;
import booking.common.dto.CreateOrderDto;
import booking.common.entity.Order;
import booking.common.entity.Room;
import booking.common.entity.User;
import booking.common.exception.EntityNotFoundException;
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
