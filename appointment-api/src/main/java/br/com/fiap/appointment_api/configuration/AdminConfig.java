package br.com.fiap.appointment_api.configuration;

import br.com.fiap.appointment_api.domain.entity.User;
import br.com.fiap.appointment_api.domain.enums.UserRole;
import br.com.fiap.appointment_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdmin() {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                userRepository.save(
                        User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin123"))
                                .role(UserRole.ADMIN)
                                .build()
                );
            }
        };
    }
}