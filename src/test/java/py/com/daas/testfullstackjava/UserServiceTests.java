package py.com.daas.testfullstackjava;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import py.com.daas.testfullstackjava.dtos.UserDto;
import py.com.daas.testfullstackjava.entities.Role;
import py.com.daas.testfullstackjava.entities.User;
import py.com.daas.testfullstackjava.entities.UserStatus;
import py.com.daas.testfullstackjava.repositories.RoleRepository;
import py.com.daas.testfullstackjava.repositories.UserRepository;
import py.com.daas.testfullstackjava.services.UserService;
import py.com.daas.testfullstackjava.services.impl.UserServiceImpl;

class UserServiceTests {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UserRepository userRepository= mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private UserService userService;
    private UserDto userDto;
    private User user;
    private Role role;
    private Long userId;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(passwordEncoder, userRepository, roleRepository);
        role = new Role("ROL_CONSULTOR", "CONSULTOR");
        userDto = new UserDto(null, "fullName", "name@gmail.com", "password", "CONSULTOR", UserStatus.ACTIVO);
        user = new User(userDto.fullName(), userDto.email(), userDto.password(),
                Collections.singletonList(role));
        userId = 2L;
    }

    @Test
    void createUserSuccess() {
        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.empty());
        when(roleRepository.findRolByName(any())).thenReturn(Optional.of(role));
        when(userRepository.save(any())).thenReturn(user);
        UserDto createdUser = userService.create(userDto);

        assertNotNull(createdUser, "created user");
    }


    @Test
    void createUserError() {
        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.create(userDto));

        Assertions.assertThat(exception.getMessage()).contains("already exists");
    }

    @Test
    void updateUserSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.of(user));
        when(roleRepository.findRolByName(any())).thenReturn(Optional.of(role));
        when(userRepository.save(any())).thenReturn(user);
        UserDto updatedUser = userService.update(userId, userDto);

        assertNotNull(updatedUser, "updated user");
    }

    @Test
    void updateUserUserNotFoundError() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(userId, userDto));

        Assertions.assertThat(exception.getMessage()).contains("not found");
    }

    @Test
    void updateUserUserWithDifferentEmailError() {
        UserDto userDto = new UserDto(null, "fullName", "name2@gmail.com", "password", "CONSULTOR", UserStatus.ACTIVO);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(userId, userDto));

        Assertions.assertThat(exception.getMessage()).contains("already exists");
    }

    @Test
    void getUserSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDto user = userService.get(userId);

        assertNotNull(user, "get user");
    }

    @Test
    void getUserError() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.get(2L));

        Assertions.assertThat(exception.getMessage()).contains("not found");
    }

}
