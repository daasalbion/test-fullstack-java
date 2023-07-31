package py.com.daas.testfullstackjava.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import py.com.daas.testfullstackjava.entities.Role;
import py.com.daas.testfullstackjava.entities.User;
import py.com.daas.testfullstackjava.entities.UserStatus;

public record UserDto(
        Long id,
        @NotEmpty(message = "fullName cannot be null") String fullName,
        @Email @NotEmpty(message = "email cannot be null") String email,
        @NotEmpty(message = "password cannot be null") String password,
        @NotEmpty(message = "password cannot be null") String role,
        UserStatus status) {
    public static UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getPassword(),
                user.getRoles().stream().map(Role::getDescription).findFirst().orElse(null), user.getStatus());
    }
}
