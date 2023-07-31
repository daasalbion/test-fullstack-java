package py.com.daas.testfullstackjava.dtos;

import py.com.daas.testfullstackjava.entities.User;
import py.com.daas.testfullstackjava.entities.UserStatus;

public record UserFilter(String fullName, String email, UserStatus status) {
    public User toUser() {
        return new User(fullName, email, status);
    }
}
