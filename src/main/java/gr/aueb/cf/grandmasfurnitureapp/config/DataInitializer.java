package gr.aueb.cf.grandmasfurnitureapp.config;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Role;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Ensures the database has at least a couple of users with properly
 * encoded (BCrypt) passwords. It also migrates any existing plaintext
 * passwords that might have been inserted via SQL scripts to BCrypt
 * on application startup.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        long count = userRepository.count();
        if (count == 0) {
            LOGGER.info("No users found. Seeding default users...");
            seedDefaultUsers();
        } else {
            LOGGER.info("{} users found. Verifying password encoding...", count);
            migratePlaintextPasswords();
        }
    }

    private void seedDefaultUsers() {
        createUser(
                "admin1",
                "Cosmote1@",
                "Maria",
                "Papadopoulos",
                "admin1@grandmasfurniture.com",
                "6932123456",
                Role.ADMIN
        );

        createUser(
                "user1",
                "Cosmote1@",
                "Elena",
                "Dimitriou",
                "elena.dimitriou@email.com",
                "6976543210",
                Role.USER
        );
    }

    private void migratePlaintextPasswords() {
        List<User> users = userRepository.findAll();
        boolean changed = false;
        for (User user : users) {
            String currentPassword = user.getPassword();
            if (currentPassword != null && !currentPassword.startsWith("$2")) {
                // Likely a plaintext password; encode it
                user.setPassword(passwordEncoder.encode(currentPassword));
                changed = true;
                LOGGER.warn("Migrated plaintext password for user '{}'.", user.getUsername());
            }
        }
        if (changed) {
            userRepository.saveAll(users);
            LOGGER.info("Password migration completed.");
        } else {
            LOGGER.info("All passwords already encoded.");
        }
    }

    private void createUser(String username,
                            String rawPassword,
                            String firstName,
                            String lastName,
                            String email,
                            String phone,
                            Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setIsActive(true);
        userRepository.save(user);
        LOGGER.info("Seeded user '{}' with role {}.", username, role);
    }
}

