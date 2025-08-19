package gr.aueb.cf.grandmasfurnitureapp.mapper;

import gr.aueb.cf.grandmasfurnitureapp.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.grandmasfurnitureapp.dto.*;
import gr.aueb.cf.grandmasfurnitureapp.model.Ad;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.City;
import gr.aueb.cf.grandmasfurnitureapp.repository.CategoryRepository;
import gr.aueb.cf.grandmasfurnitureapp.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Mapper component for converting between DTOs and entities.
 * Handles bidirectional mapping between API DTOs and JPA entities.
 * 
 * <p>This mapper provides methods to convert between different data representations:
 * <ul>
 *   <li>Entity to DTO mapping for API responses</li>
 *   <li>DTO to Entity mapping for API requests</li>
 *   <li>Static data mapping (Category, City)</li>
 * </ul></p>
 * 
 * <p>The mapper also handles entity lookups for related objects (Category, City)
 * when creating new entities from DTOs.</p>
 * 
 * @author Giannis Gotzaridis
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;

    /**
     * Maps an Ad entity to its read-only DTO representation.
     * 
     * <p>This method converts a JPA Ad entity to a DTO suitable for API responses.
     * It includes all ad details, related category and city information, user details,
     * and audit fields.</p>
     * 
     * @param ad The Ad entity to convert
     * @return AdReadOnlyDTO containing all ad information for API responses
     */
    public AdReadOnlyDTO mapToAdReadOnlyDTO(Ad ad) {
        AdReadOnlyDTO adReadOnlyDTO = new AdReadOnlyDTO();

        adReadOnlyDTO.setId(ad.getId());
        adReadOnlyDTO.setTitle(ad.getTitle());

        // Map category to DTO (no duplicate audit fields)
        if (ad.getCategory() != null) {
            CategoryReadOnlyDTO categoryReadOnlyDTO = new CategoryReadOnlyDTO();
            categoryReadOnlyDTO.setId(ad.getCategory().getId());
            categoryReadOnlyDTO.setCategory(ad.getCategory().getCategory());
            adReadOnlyDTO.setCategory(categoryReadOnlyDTO);
        }

        // Map city to DTO (no duplicate audit fields)
        if (ad.getCity() != null) {
            CityReadOnlyDTO cityReadOnlyDTO = new CityReadOnlyDTO();
            cityReadOnlyDTO.setId(ad.getCity().getId());
            cityReadOnlyDTO.setCityName(ad.getCity().getCityName());
            adReadOnlyDTO.setCity(cityReadOnlyDTO);
        }

        adReadOnlyDTO.setCondition(ad.getCondition());
        adReadOnlyDTO.setPrice(ad.getPrice());
        adReadOnlyDTO.setIsAvailable(ad.getIsAvailable());
        adReadOnlyDTO.setDescription(ad.getDescription());
        adReadOnlyDTO.setImagePath(ad.getImage() != null ? ad.getImage().getFilePath() : null);

        // Only Ad's audit fields - no duplicates
        adReadOnlyDTO.setCreatedAt(ad.getCreatedAt());
        adReadOnlyDTO.setUpdatedAt(ad.getUpdatedAt());

        // Add user info if needed
        if (ad.getUser() != null) {
            adReadOnlyDTO.setUserFirstName(ad.getUser().getFirstName());
            adReadOnlyDTO.setUserLastName(ad.getUser().getLastName());
            adReadOnlyDTO.setUserPhone(ad.getUser().getPhone());
        }

        return adReadOnlyDTO;
    }

    /**
     * Maps an AdInsertDTO to an Ad entity for creation.
     * 
     * <p>This method converts a DTO from an API request to a JPA entity for persistence.
     * It performs lookups for related entities (Category, City) by name and sets
     * all the basic ad properties.</p>
     * 
     * <p>Note: The returned entity is not persisted and needs to be saved separately.
     * The user relationship should be set by the calling service.</p>
     * 
     * @param dto The DTO containing ad creation data
     * @return Ad entity ready for persistence
     * @throws AppObjectNotFoundException if Category or City lookup fails
     */
    public Ad mapToAdEntity(AdInsertDTO dto) throws AppObjectNotFoundException {
        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());

        // Look up category by name
        if (dto.getCategoryName() != null) {
            Category category = categoryRepository.findByCategory(dto.getCategoryName())
                    .orElseThrow(() -> new AppObjectNotFoundException("Category", "Category not found: " + dto.getCategoryName()));
            ad.setCategory(category);
        }

        // Look up city by name
        if (dto.getCityName() != null) {
            City city = cityRepository.findByCityName(dto.getCityName())
                    .orElseThrow(() -> new AppObjectNotFoundException("City", "City not found: " + dto.getCityName()));
            ad.setCity(city);
        }

        ad.setCondition(dto.getCondition());
        ad.setPrice(dto.getPrice());
        ad.setIsAvailable(dto.getIsAvailable());
        ad.setDescription(dto.getDescription());

        return ad;
    }

    /**
     * Maps a UserInsertDTO to a User entity for creation.
     * 
     * <p>This method converts a DTO from user registration to a JPA entity.
     * It encrypts the password using BCrypt and sets default values for new users.</p>
     * 
     * @param dto The DTO containing user registration data
     * @return User entity ready for persistence with encrypted password
     */
    public User mapToUserEntity(UserInsertDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setIsActive(true); // Set default active status

        return user;
    }

    /**
     * Maps a User entity to its read-only DTO representation.
     * 
     * <p>This method converts a JPA User entity to a DTO suitable for API responses.
     * It includes all user details except the password for security reasons.</p>
     * 
     * @param user The User entity to convert
     * @return UserReadOnlyDTO containing user information for API responses
     */
    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        UserReadOnlyDTO userReadOnlyDTO = new UserReadOnlyDTO();

        userReadOnlyDTO.setId(user.getId());
        userReadOnlyDTO.setUsername(user.getUsername());
        userReadOnlyDTO.setFirstname(user.getFirstName());
        userReadOnlyDTO.setLastname(user.getLastName());
        userReadOnlyDTO.setEmail(user.getEmail());
        userReadOnlyDTO.setPhone(user.getPhone());
        userReadOnlyDTO.setRole(user.getRole() != null ? user.getRole().name() : null);
        userReadOnlyDTO.setIsActive(user.getIsActive());

        return userReadOnlyDTO;
    }

    /**
     * Maps a Category entity to its read-only DTO representation.
     * 
     * @param category The Category entity to convert, or null
     * @return CategoryReadOnlyDTO containing category information, or null if input is null
     */
    public CategoryReadOnlyDTO mapToCategoryDTO(gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category category) {
        if (category == null) return null;

        CategoryReadOnlyDTO dto = new CategoryReadOnlyDTO();
        dto.setId(category.getId());
        dto.setCategory(category.getCategory());
        return dto;
    }

    /**
     * Maps a City entity to its read-only DTO representation.
     * 
     * @param city The City entity to convert, or null
     * @return CityReadOnlyDTO containing city information, or null if input is null
     */
    public CityReadOnlyDTO mapToCityDTO(gr.aueb.cf.grandmasfurnitureapp.model.static_data.City city) {
        if (city == null) return null;

        CityReadOnlyDTO dto = new CityReadOnlyDTO();
        dto.setId(city.getId());
        dto.setCityName(city.getCityName());
        return dto;
    }
}