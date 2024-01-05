package booking.common.repository;

import booking.common.entity.Role;
import booking.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    Optional<User> findUserById(long id);

    @Modifying(clearAutomatically = true)
    @Query("update User u " +
           "set u.role = :role " +
           "where u.login = :login")
    int updateRole(Role role, String login);
}
