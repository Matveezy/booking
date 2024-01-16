package booking.hotel.service;

import booking.hotel.dto.HotelCreateDto;
import booking.hotel.dto.HotelInfoDto;
import booking.hotel.dto.HotelUpdateDto;
import booking.hotel.entity.Hotel;
import booking.hotel.exception.NotEnoughPermissionException;
import booking.hotel.mapper.HotelCreateMapper;
import booking.hotel.mapper.HotelReadMapper;
import booking.hotel.repo.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelCreateMapper hotelCreateMapper;
    private final HotelReadMapper hotelReadMapper;


    @Transactional
    public HotelInfoDto createHotel(Long userId, HotelCreateDto hotelCreateDto) {
        Hotel hotelToCreate = hotelCreateMapper.mapToEntity(hotelCreateDto);
        hotelToCreate.setUserId(List.of(userId));
        hotelRepository.save(hotelToCreate);
        return hotelReadMapper.mapToDto(hotelToCreate);
    }

    @Transactional
    public HotelInfoDto updateHotel(Long userId, HotelUpdateDto hotelUpdateDto) {
        Long hotelId = hotelUpdateDto.getId();
        return hotelRepository.findById(hotelId).map(hotel -> {
            if (!hotel.getUserId().contains(userId))
                throw new NotEnoughPermissionException("You have no permission to update this hotel");

            if (hotelUpdateDto.getCity() != null) hotel.setCity(hotelUpdateDto.getCity());
            if (hotelUpdateDto.getHotelClass() != null) hotel.setHotelClass(hotelUpdateDto.getHotelClass());
            if (hotelUpdateDto.getName() != null) hotel.setName(hotelUpdateDto.getName());

            hotelRepository.save(hotel);
            return hotelReadMapper.mapToDto(hotel);
        }).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    public boolean deleteHotel(Long userId, Long hotelId) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        return hotelOptional.map(hotel -> {
            if (!hotel.getUserId().contains(userId))
                throw new NotEnoughPermissionException("You have no permission to delete this hotel");
            hotelRepository.delete(hotel);
            return true;
        }).orElse(false);
    }

    public List<HotelInfoDto> findAll() {
        return hotelRepository.findAll().stream()
                .map(hotelReadMapper::mapToDto).toList();
    }

    public Optional<Hotel> findHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }
}
