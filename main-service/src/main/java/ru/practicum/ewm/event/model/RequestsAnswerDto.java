package ru.practicum.ewm.event.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestsAnswerDto {
    private List<Integer> requestIds;
    private RequestAnswerState status;
}
