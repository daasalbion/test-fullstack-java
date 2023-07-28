package py.com.daas.testfullstackjava.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import py.com.daas.testfullstackjava.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findAll();

}
