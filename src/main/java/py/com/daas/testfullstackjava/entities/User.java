package py.com.daas.testfullstackjava.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String password;
    @Convert(converter = UserStatusConverter.class)
    private UserStatus status;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns
            = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
    private List<Role> roles;

    public User() {}

    public User(String fullName, String email, UserStatus status) {
        this.fullName = fullName;
        this.email = email;
        this.status = status;
    }

    public User(Long id, String fullName, String email, UserStatus status) {
        this(fullName, email, status);
        this.id = id;
    }

    public User(String fullName, String email, String password, List<Role> roles) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

}
