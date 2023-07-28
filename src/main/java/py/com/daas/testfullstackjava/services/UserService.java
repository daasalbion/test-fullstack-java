package py.com.daas.testfullstackjava.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import py.com.daas.testfullstackjava.entities.User;

public interface UserService extends UserDetailsService {

    Optional<User> getByUsername(String username);
    User create(User user);
    User get(Long id);
    void delete(Long id);
    List<User> getAll();

}
