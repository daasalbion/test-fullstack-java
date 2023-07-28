package py.com.daas.testfullstackjava.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import py.com.daas.testfullstackjava.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findRolByName(String name);
}
