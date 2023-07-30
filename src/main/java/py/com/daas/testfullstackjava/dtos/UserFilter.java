package py.com.daas.testfullstackjava.dtos;

import py.com.daas.testfullstackjava.entities.User;

public record UserFilter(String fullName, String email, String status) {
    public User toUser() {
        return new User(fullName, email, status);
    }
}
