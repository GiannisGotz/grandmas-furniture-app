package gr.aueb.cf.grandmasfurnitureapp.mapper;

import gr.aueb.cf.grandmasfurnitureapp.dto.AdInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserInsertDTO;
import gr.aueb.cf.grandmasfurnitureapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.model.Ad;
import gr.aueb.cf.grandmasfurnitureapp.dto.AdReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;
    public AdReadOnlyDTO mapToAdReadOnlyDTO(Ad ad) {
        AdReadOnlyDTO adReadOnlyDTO = new AdReadOnlyDTO();

        adReadOnlyDTO.setId(ad.getId());
        adReadOnlyDTO.setTitle(ad.getTitle());
        adReadOnlyDTO.setCategory(ad.getCategory());
        adReadOnlyDTO.setCondition(ad.getCondition());
        adReadOnlyDTO.setPrice(ad.getPrice());
        adReadOnlyDTO.setIsAvailable(ad.isAvailable());
        adReadOnlyDTO.setDescription(ad.getDescription());
        adReadOnlyDTO.setImagePath(ad.getImage() != null ? ad.getImage().getImagePath() : null);
        adReadOnlyDTO.setCreatedAt(ad.getCreatedAt());
        adReadOnlyDTO.setUpdatedAt(ad.getUpdatedAt());

        return adReadOnlyDTO;
    }

    public Ad mapToAdEntity(AdInsertDTO dto) {
        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setCategory(dto.getCategory());
        ad.setCondition(dto.getCondition());
        ad.setPrice(dto.getPrice());
        ad.setAvailable(dto.isAvailable());
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
}
