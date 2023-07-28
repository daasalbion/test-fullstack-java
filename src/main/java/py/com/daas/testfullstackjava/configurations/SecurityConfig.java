package py.com.daas.testfullstackjava.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import py.com.daas.testfullstackjava.repositories.RoleRepository;
import py.com.daas.testfullstackjava.repositories.UserRepository;
import py.com.daas.testfullstackjava.services.UserService;
import py.com.daas.testfullstackjava.services.impl.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final RoleRepository rolRepository;

    public SecurityConfig(UserRepository userRepository, RoleRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // public routes
                .authorizeHttpRequests()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
                .and()
                // enable basic-auth and ROLE_USER for all other routes
                .authorizeHttpRequests().anyRequest().permitAll()
                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl(passwordEncoder(), userRepository, rolRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
