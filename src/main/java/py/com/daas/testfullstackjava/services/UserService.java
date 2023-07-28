package py.com.daas.testfullstackjava.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import py.com.daas.testfullstackjava.entities.User;
import py.com.daas.testfullstackjava.entities.UserFilter;

public interface UserService extends UserDetailsService {

    Optional<User> getByUsername(String username);
    User create(User user);
    User update(User user);
    User get(Long id);
    void delete(Long id);
    Page<User> findAll(UserFilter userFilter, Pageable pageable);

}
