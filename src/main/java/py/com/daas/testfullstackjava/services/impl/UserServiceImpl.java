package py.com.daas.testfullstackjava.services.impl;

import static java.util.Objects.isNull;
import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import py.com.daas.testfullstackjava.entities.Role;
import py.com.daas.testfullstackjava.entities.User;
import py.com.daas.testfullstackjava.entities.UserFilter;
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
    public Optional<User> getByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUsername(username)
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
    public User create(User user) {
        if (getByUsername(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("ACTIVO");
        Role baseRol = roleRepository.findRolByName("ROLE_CONSULTOR")
                .orElseThrow(() -> new IllegalArgumentException("Rol ROLE_CONSULTOR is not present"));
        user.setRoles(Collections.singletonList(baseRol));

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User updatedUser) {
        User user = get(id);
        if (!updatedUser.getEmail().equals(user.getEmail()) && getByUsername(updatedUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        if (!isNull(updatedUser.getFullName())) {
            user.setFullName(updatedUser.getFullName());
        }
        if (!isNull(updatedUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (!isNull(updatedUser.getStatus())) {
            user.setStatus(updatedUser.getStatus());
        }

        return userRepository.save(user);
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public void delete(Long id) {
        User user = get(id);
        userRepository.delete(user);
    }

    @Override
    public Page<User> findAll(UserFilter userFilter, Pageable pageable) {
        User user = new User();
        user.setFullName(userFilter.fullName());
        user.setStatus(userFilter.status());
        user.setEmail(userFilter.email());
        Example<User> userExample = Example.of(user);

        return userRepository.findAll(userExample, pageable);
    }

}
