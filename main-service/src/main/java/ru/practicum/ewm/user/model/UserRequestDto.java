package ru.practicum.ewm.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserRequestDto {
    @NotBlank
    @Email
    @Size(min = 4, max = 254)
    private String email;
    @Size(min = 2, max = 250)
    @NotBlank
    private String name;
}
