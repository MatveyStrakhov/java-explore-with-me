package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.model.UserDto;
import ru.practicum.ewm.user.model.UserRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController()
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/admin/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids, @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(required = false, value = "size", defaultValue = "10") Integer size) {
        return userService.returnListOfUserDto(ids, from, size);

    }

    @PostMapping(path = "/admin/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequestDto));
    }

    @DeleteMapping(path = "/admin/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
