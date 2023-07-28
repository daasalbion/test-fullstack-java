package py.com.daas.testfullstackjava.dtos;

import jakarta.validation.constraints.NotEmpty;

public record LoginDto(
        @NotEmpty(message = "username cannot be null") String username,
        @NotEmpty(message = "password cannot be null") String password) {
}
