package py.com.daas.testfullstackjava.dtos;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(
        @NotEmpty(message = "fullName cannot be null") String fullName,
        @NotEmpty(message = "username cannot be null") String username,
        @NotEmpty(message = "password cannot be null") String password,
        String status) {

}
