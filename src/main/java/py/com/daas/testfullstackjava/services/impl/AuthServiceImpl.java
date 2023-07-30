package py.com.daas.testfullstackjava.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import py.com.daas.testfullstackjava.dtos.JwtResponse;
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
    public Optional<JwtResponse> login(LoginDto loginDto) {
        LOGGER.info("User = {} attempting to log in", loginDto.username());
        UserDetails userDetails = userService.loadUserByUsername(loginDto.username());
        Authentication authentication = authenticate(loginDto);
        String jwt = generateToken(authentication);

        return Optional.of(new JwtResponse(jwt, null, userDetails.getUsername(), userDetails.getUsername(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));
    }

    private Authentication authenticate(LoginDto loginDto) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(),
                loginDto.password()));
    }

    private String generateToken(Authentication authentication) {
        return jwtService.generateToken(authentication.getName(), authentication.getAuthorities());
    }

}
