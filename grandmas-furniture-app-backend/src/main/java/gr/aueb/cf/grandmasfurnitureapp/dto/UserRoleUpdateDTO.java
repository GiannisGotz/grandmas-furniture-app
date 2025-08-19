package gr.aueb.cf.grandmasfurnitureapp.dto;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for updating user role operations.
 * Contains the new role information to be updated.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRoleUpdateDTO {
    
    @NotNull(message = "Role is required")
    private Role role;
}