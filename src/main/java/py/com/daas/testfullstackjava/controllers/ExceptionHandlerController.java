package py.com.daas.testfullstackjava.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(IllegalArgumentException.class)
    void handleBadRequest(HttpServletResponse req, Exception ex) throws IOException {
        req.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    void handleMethodArgumentNotValidException(HttpServletResponse req,
            MethodArgumentNotValidException ex) throws IOException {
        req.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    void handleMethodAuthenticationException(HttpServletResponse req,
            AuthenticationException ex) throws IOException {
        req.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

}
