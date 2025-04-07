package gr.hephaestus.weatherstation.service.config.security;

/*
 *
 *  @Code from GitHub repository :  https://github.com/xartokoptiko/spring-jwt-conf
 *
 */

import gr.hephaestus.weatherstation.service.config.security.entities.Role;
import gr.hephaestus.weatherstation.service.config.security.entities.User;
import gr.hephaestus.weatherstation.service.config.security.repositories.RoleRepository;
import gr.hephaestus.weatherstation.service.config.security.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if (userRepository.findByEmail("[YOUR EMAIL]").isEmpty()){
            User user = new User();
            user.setEmail("[YOUR EMAIL]");
            user.setFullName("[YOUR NAME]");
            user.setPassword(passwordEncoder.encode("[OUR PASSWORD]"));

            Role userRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRoles(Set.of(userRole));

            userRepository.save(user);
        }

    }
}
