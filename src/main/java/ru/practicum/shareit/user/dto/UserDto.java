package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.MarkerValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = {MarkerValidation.OnCreate.class})
    @Email(message = "Email имеет некорректный формат: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            groups = {MarkerValidation.OnCreate.class, MarkerValidation.OnUpdate.class})
    private String email;
    @NotBlank(groups = {MarkerValidation.OnCreate.class})
    private String name;

}