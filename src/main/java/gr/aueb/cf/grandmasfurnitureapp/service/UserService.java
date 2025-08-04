package gr.aueb.cf.grandmasfurnitureapp.service;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.mapper.Mapper;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Transactional
    public UserReadOnlyDTO registerUser(UserInsertDTO userInsertDTO) throws AppObjectAlreadyExists {

        // Check for duplicate username
        if (userRepository.findByUsername(userInsertDTO.getUsername()).isPresent()) {
            throw new AppObjectAlreadyExists("User", "User with username " + userInsertDTO.getUsername()
                    + " already exists");
        }

        // Check for duplicate email
        if (userRepository.findByEmail(userInsertDTO.getEmail()).isPresent()) {
            LOGGER.error("User with email {} already exists", userInsertDTO.getEmail());
            throw new AppObjectAlreadyExists("User", "User with email " + userInsertDTO.getEmail()
                    + " already exists");
        }


        // Map DTO to entity and save
        LOGGER.info("Mapping DTO to entity: {}", userInsertDTO);
        User user = mapper.mapToUserEntity(userInsertDTO);

        try {
            // Persist the new user entity to the database
            User savedUser = userRepository.save(user);

            // Return the saved user as a read-only DTO
            return mapper.mapToUserReadOnlyDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Data integrity violation while user registration : {}", user, e);
            throw new AppObjectAlreadyExists("User", "Duplicate user data found");
        }
    }

    @Transactional
    public void deleteUser(String username) throws AppObjectAlreadyExists {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectAlreadyExists("User", "User with username: " + username + " not found"));

        // Perform user deletion
        userRepository.delete(user);
        LOGGER.info("User with username {} successfully deleted", username);
    }

}
