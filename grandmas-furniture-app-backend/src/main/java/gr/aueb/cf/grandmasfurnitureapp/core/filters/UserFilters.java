package gr.aueb.cf.grandmasfurnitureapp.core.filters;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Role;
import org.springframework.data.domain.Pageable;
import jakarta.annotation.Nullable;
import lombok.*;

/**
 * Filter criteria for user search operations.
 * Contains optional filters for user attributes and status.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserFilters extends GenericFilters {

    @Nullable
    private String username;

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String email;

    @Nullable
    private String phone;

    @Nullable
    private Role role;

    @Nullable
    private Boolean isActive;

}
