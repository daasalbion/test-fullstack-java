package py.com.daas.testfullstackjava.repositories;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import py.com.daas.testfullstackjava.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Page<User> findAll(Example<User> example, Pageable pageable);

}
