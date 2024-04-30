package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsRequestDto {
    @JsonProperty(value = "start")
    private String start;
    @JsonProperty(value = "end")
    private String end;
    @JsonProperty(value = "uris")
    private List<String> uris;
    @JsonProperty(value = "unique")
    private boolean unique;

}
