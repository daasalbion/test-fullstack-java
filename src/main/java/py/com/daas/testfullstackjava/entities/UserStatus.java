package py.com.daas.testfullstackjava.entities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO");
    private final String name;
    private static final Map<String, UserStatus> BY_NAME = new HashMap<>();

    static {
        for (UserStatus value : UserStatus.values()) {
            BY_NAME.put(value.getName().toLowerCase(), value);
        }
    }

    UserStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public static UserStatus find(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return BY_NAME.get(name.toLowerCase());
    }
}
