package py.com.daas.testfullstackjava.services.impl;

import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import py.com.daas.testfullstackjava.dtos.UserDto;
import py.com.daas.testfullstackjava.dtos.UserFilter;
import py.com.daas.testfullstackjava.entities.Role;
import py.com.daas.testfullstackjava.entities.User;
import py.com.daas.testfullstackjava.repositories.RoleRepository;
import py.com.daas.testfullstackjava.repositories.UserRepository;
import py.com.daas.testfullstackjava.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository,
            RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndStatus(username, "ACTIVO")
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with " +
                        "fullName %s does not exist", username)));
        List<SimpleGrantedAuthority> userAuthorities = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(userAuthorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (getByUsername(userDto.email()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = new User(userDto.fullName(), userDto.email(), userDto.status());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRoles(getRolByName(userDto.role()));

        return UserDto.fromUser(userRepository.save(user));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User storedUser = getById(id);
        if (!storedUser.getEmail().equals(userDto.email()) && getByUsername(userDto.email()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = new User(storedUser.getId(), userDto.fullName(), userDto.email(), userDto.status());
        user.setPassword(storedUser.getPassword());
        user.setRoles(storedUser.getRoles());

        if (!storedUser.getPassword().equals(userDto.password())) {
            user.setPassword(passwordEncoder.encode(userDto.password()));
        }
        String role = storedUser.getRoles().stream().map(Role::getDescription).collect(Collectors.joining());
        if (!role.equals(userDto.role())) {
            user.setRoles(getRolByName(userDto.role()));
        }

        return UserDto.fromUser(userRepository.save(user));
    }

    @Override
    public UserDto get(Long id) {
        User user = getById(id);
        return UserDto.fromUser(user);
    }

    @Override
    public void delete(Long id) {
        User user = getById(id);
        userRepository.delete(user);
    }

    @Override
    public Page<UserDto> findAll(UserFilter userFilter, Pageable pageable) {
        User user = userFilter.toUser();
        Example<User> userExample = Example.of(user);
        Page<User> userPage = userRepository.findAll(userExample, pageable);
        List<UserDto> userDtoList = userPage.stream()
                .map(UserDto::fromUser)
                .toList();

        return new PageImpl<>(userDtoList, pageable, userPage.getTotalElements());
    }

    private User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Optional<User> getByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    private List<Role> getRolByName(String roleName) {
        Role role = roleRepository.findRolByName(String.format("ROLE_%s", roleName))
                .orElseThrow(() -> new IllegalArgumentException("Rol is not present"));
        return Collections.singletonList(role);
    }

}
