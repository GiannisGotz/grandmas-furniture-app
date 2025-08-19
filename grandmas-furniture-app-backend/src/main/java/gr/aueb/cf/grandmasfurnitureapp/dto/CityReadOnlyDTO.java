package gr.aueb.cf.grandmasfurnitureapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for city data retrieval operations.
 * Contains read-only city information for API responses.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CityReadOnlyDTO {
    private Long id;
    private String cityName;
}