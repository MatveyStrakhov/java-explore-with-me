package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.model.RequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getOwnRequests(@PathVariable @NotNull long userId) {
        return requestService.getOwnRequests(userId);
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<?> createRequest(@PathVariable @NotNull long userId, @RequestParam @NotNull long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.createRequest(userId, eventId));
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable @NotNull long userId, @PathVariable @NotNull long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

}
