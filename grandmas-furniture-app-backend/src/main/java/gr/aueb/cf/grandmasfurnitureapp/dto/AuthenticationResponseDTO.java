package gr.aueb.cf.grandmasfurnitureapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response data.
 * Contains user information and JWT token for successful login.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private String firstname;
    private String lastname;
    private String token;
    private String role;
}