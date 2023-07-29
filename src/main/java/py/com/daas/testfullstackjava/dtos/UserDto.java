package py.com.daas.testfullstackjava.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserDto(
        Long id,
        @NotEmpty(message = "fullName cannot be null") String fullName,
        @Email @NotEmpty(message = "username cannot be null") String username,
        @JsonIgnore
        @NotEmpty(message = "password cannot be null") String password,
        @NotEmpty(message = "password cannot be null") String role,
        String status) {
}
