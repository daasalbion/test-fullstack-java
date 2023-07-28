package py.com.daas.testfullstackjava.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.InvalidKeyException;
import py.com.daas.testfullstackjava.dtos.LoginDto;
import py.com.daas.testfullstackjava.services.AuthService;
import py.com.daas.testfullstackjava.services.UserService;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthServiceImpl(
        AuthenticationManager authenticationManager,
        UserService userService,
        JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Optional<String> login(LoginDto loginDto) {
        LOGGER.info("User = {} attempting to log in", loginDto.username());
        return Optional.of(userService.loadUserByUsername(loginDto.username()))
                .flatMap(u -> authenticate(loginDto))
                .flatMap(this::generateToken);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    private Optional<Authentication> authenticate(LoginDto loginDto) {
        try {
            return Optional.of(authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password())));
        } catch (AuthenticationException ex) {
            LOGGER.error("Authentication failed for user = {}", loginDto.username(), ex);
            return Optional.empty();
        }
    }

    private Optional<String> generateToken(Authentication authentication) {
        try {
            return Optional.of(jwtService.generateToken(authentication.getName(), authentication.getAuthorities()));
        } catch (InvalidKeyException ex) {
            LOGGER.error("Token generation failed for user = {}", authentication.getName(), ex);
            return Optional.empty();
        }
    }

}
