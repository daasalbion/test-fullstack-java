package py.com.daas.testfullstackjava.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import py.com.daas.testfullstackjava.dtos.UserDto;
import py.com.daas.testfullstackjava.dtos.UserFilter;

public interface UserService extends UserDetailsService {

    UserDto create(UserDto user);
    UserDto update(Long id, UserDto user);
    UserDto get(Long id);
    void delete(Long id);
    Page<UserDto> findAll(UserFilter userFilter, Pageable pageable);

}
