package ru.practicum.ewm.event.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.practicum.ewm.request.model.RequestDto;

import java.util.List;

@Data
@Builder
@ToString
public class AnswerRequestResultDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
