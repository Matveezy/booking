package booking.hotel.service;

import booking.hotel.entity.User;
import booking.hotel.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(long id) {
        return userRepository.findById(id).get();
    }
}
