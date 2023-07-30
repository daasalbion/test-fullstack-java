package py.com.daas.testfullstackjava.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.validation.Valid;
import py.com.daas.testfullstackjava.dtos.JwtResponse;
import py.com.daas.testfullstackjava.dtos.LoginDto;
import py.com.daas.testfullstackjava.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid LoginDto loginDto) {
        return service.login(loginDto)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
    }

}
