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

@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;

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

    // Look up city by name
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





    public User mapToUserEntity(UserInsertDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());

        return user;
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        UserReadOnlyDTO userReadOnlyDTO = new UserReadOnlyDTO();

        userReadOnlyDTO.setFirstname(user.getFirstName());
        userReadOnlyDTO.setLastname(user.getLastName());
        userReadOnlyDTO.setEmail(user.getEmail());

        return userReadOnlyDTO;
    }

    // Utility mappers for Category and City
    public CategoryReadOnlyDTO mapToCategoryDTO(gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category category) {
        if (category == null) return null;

        CategoryReadOnlyDTO dto = new CategoryReadOnlyDTO();
        dto.setId(category.getId());
        dto.setCategory(category.getCategory());
        return dto;
    }

    public CityReadOnlyDTO mapToCityDTO(gr.aueb.cf.grandmasfurnitureapp.model.static_data.City city) {
        if (city == null) return null;

        CityReadOnlyDTO dto = new CityReadOnlyDTO();
        dto.setId(city.getId());
        dto.setCityName(city.getCityName());
        return dto;
    }
}