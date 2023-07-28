package py.com.daas.testfullstackjava.services;

import java.util.Optional;

import py.com.daas.testfullstackjava.dtos.JwtResponse;
import py.com.daas.testfullstackjava.dtos.LoginDto;

public interface AuthService {

    Optional<JwtResponse> login(LoginDto loginDto);
    boolean validateToken(String token);

}
