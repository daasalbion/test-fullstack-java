package py.com.daas.testfullstackjava.services;

import java.util.Optional;

import py.com.daas.testfullstackjava.dtos.LoginDto;

public interface AuthService {

    Optional<String> login(LoginDto loginDto);
    boolean validateToken(String token);

}
