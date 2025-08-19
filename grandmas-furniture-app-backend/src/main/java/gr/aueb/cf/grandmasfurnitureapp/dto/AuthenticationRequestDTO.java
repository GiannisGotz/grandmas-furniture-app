package gr.aueb.cf.grandmasfurnitureapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication request data.
 * Contains username and password for login operations.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {

    @NotNull
    @Schema(example = "Admin1", description = "Username for authentication")
    private String username;
    @NotNull
    @Schema(example = "Cosmote1@", description = "Password for authentication")
    private String password;
}
