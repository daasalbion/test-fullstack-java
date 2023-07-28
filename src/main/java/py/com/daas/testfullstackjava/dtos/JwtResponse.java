package py.com.daas.testfullstackjava.dtos;

import java.util.List;

public record JwtResponse(String accessToken, Long id, String fullName, String username, List<String> roles) {
}
