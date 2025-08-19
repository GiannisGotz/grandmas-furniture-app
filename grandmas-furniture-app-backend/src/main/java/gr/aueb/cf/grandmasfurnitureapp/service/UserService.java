package gr.aueb.cf.grandmasfurnitureapp.service;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.grandmasfurnitureapp.core.filters.Paginated;
import gr.aueb.cf.grandmasfurnitureapp.core.filters.UserFilters;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.mapper.Mapper;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.repository.UserRepository;
import gr.aueb.cf.grandmasfurnitureapp.core.specifications.UserSpecification;
import gr.aueb.cf.grandmasfurnitureapp.core.enums.Role;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for user management operations.
 * Handles user registration, retrieval, and filtering with pagination support.
 */
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


    /**
     * Find user by id
     *
     * @param id
     * @return
     * @throws AppObjectNotFoundException
     */
    public User findById(Long id) throws AppObjectNotFoundException {
        LOGGER.info("Finding user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("User with ID {} not found", id);
                    return new AppObjectNotFoundException("User", "User with id " + id + " not found");
                });
    }


    /**
     * Delete user by username
     *
     * @param username
     * @throws AppObjectNotFoundException
     */
    @Transactional
    public void deleteUser(String username) throws AppObjectNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with username: " + username + " not found"));

        try {
            // Perform user deletion
            userRepository.delete(user);
            LOGGER.info("User with username {} successfully deleted", username);
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Cannot delete user {} - has active ads", username);
            throw new DataIntegrityViolationException("Cannot delete user who has active ads. Please delete their ads first.");
        }
    }

    /**
     * Get paginated users with default sorting
     *
     * @param page page number (0-based)
     * @param size number of users per page
     * @return paginated users as DTOs
     */
    @Transactional
    public Page<UserReadOnlyDTO> getPaginatedUsers(int page, int size) {
        LOGGER.info("Fetching paginated users - page: {}, size: {}", page, size);
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }


    /**
     * Get paginated users with custom sorting
     *
     * @param page          page number (0-based)
     * @param size          number of users per page
     * @param sortBy        field to sort by
     * @param sortDirection sorting direction (asc/desc)
     * @return paginated and sorted users as DTOs
     */
    @Transactional
    public Page<UserReadOnlyDTO> getPaginatedSortedUsers(int page, int size, String sortBy, String sortDirection) {
        LOGGER.info("Fetching paginated users - page: {}, size: {}, sortby: {}, sortDirection: {}", page, size, sortBy, sortDirection);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable).map(mapper::mapToUserReadOnlyDTO);
    }


    /**
     * Retrieves a filtered and paginated list of users based on provided filters.
     *
     * @param filters The filters to apply for user retrieval.
     * @return Paginated list of UserReadOnlyDTOs matching the filters.
     */
    @Transactional
    public List<UserReadOnlyDTO> getUsersFiltered(UserFilters filters) {
        var filtered = userRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToUserReadOnlyDTO)).getData();
    }


    /**
     * Builds user filtering specifications based on provided filter criteria.
     *
     * @param filters The filters to apply.
     * @return Specification for filtering users.
     */
    private Specification<User> getSpecsFromFilters(UserFilters filters) {
        return Specification
                .where(UserSpecification.userEmailIs(filters.getEmail()))
                .and(UserSpecification.userIsActive(filters.getIsActive()));
    }

    /**
     * Update user role by user ID
     *
     * @param userId user ID
     * @param newRole new role to assign
     * @throws AppObjectNotFoundException if user not found
     */
    @Transactional
    public void updateUserRole(Long userId, Role newRole) throws AppObjectNotFoundException {
        LOGGER.info("Updating user role - userId: {}, newRole: {}", userId, newRole);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("User with ID {} not found", userId);
                    return new AppObjectNotFoundException("User", "User with id " + userId + " not found");
                });

        user.setRole(newRole);
        userRepository.save(user);
        
        LOGGER.info("User with ID {} role successfully updated to {}", userId, newRole);
    }
}








