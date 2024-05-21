package ru.practicum.ewm.user;

import java.util.List;
import ru.practicum.ewm.user.model.UserDto;
import ru.practicum.ewm.user.model.UserRequestDto;

public interface UserService {
    List<UserDto> returnListOfUserDto(List<Long> ids, Integer from, Integer size);

    UserDto createUser(UserRequestDto userRequestDto);

    void deleteUserById(long id);
}
