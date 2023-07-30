package py.com.daas.testfullstackjava.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import py.com.daas.testfullstackjava.dtos.UserDto;
import py.com.daas.testfullstackjava.dtos.UserFilter;
import py.com.daas.testfullstackjava.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody @Valid UserDto user) {
        return userService.update(id, user);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @GetMapping
    public Page<UserDto> list(@RequestParam(value = "filter", required = false) String filter, Pageable pageable)
            throws JsonProcessingException {
        UserFilter userFilter = new UserFilter(null, null, null);
        if (filter != null) {
            userFilter = objectMapper.readValue(filter, UserFilter.class);
        }

        return userService.findAll(userFilter, pageable);
    }
}
