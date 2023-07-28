package py.com.daas.testfullstackjava.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.validation.Valid;
import py.com.daas.testfullstackjava.dtos.LoginDto;
import py.com.daas.testfullstackjava.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginDto loginDto) {
        return service.login(loginDto)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam("token") String token) {
        return service.validateToken(token);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return exception.getMessage();
    }

}
