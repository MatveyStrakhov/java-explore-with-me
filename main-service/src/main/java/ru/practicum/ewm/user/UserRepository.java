package ru.practicum.ewm.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.UserDto;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select new ru.practicum.ewm.user.model.UserDto(u.id, u.email, u.name) from User u where u.id in ?1")
    List<UserDto> returnUsersByPageAndIds(List<Long> ids, PageRequest pageRequest);

    @Query("select u.id from User u")
    List<Long> returnListOfIds();

    @Query("select new ru.practicum.ewm.user.model.UserDto(u.id, u.email, u.name) from User u")
    List<UserDto> returnUsersByPage(PageRequest pageRequest);
}
