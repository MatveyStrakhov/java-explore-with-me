package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.UserNotFoundException;
import ru.practicum.ewm.user.model.UserDto;
import ru.practicum.ewm.user.model.UserMapper;
import ru.practicum.ewm.user.model.UserRequestDto;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> returnListOfUserDto(List<Long> ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (ids != null && !ids.isEmpty()) {
            log.error(ids.toString());
            return userRepository.returnUsersByPageAndIds(ids, pageRequest);
        } else return userRepository.returnUsersByPage(pageRequest);
    }

    @Override
    public UserDto createUser(UserRequestDto userRequestDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userRequestDto)));
    }

    @Override
    public void deleteUserById(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else throw new UserNotFoundException(id);

    }
}
