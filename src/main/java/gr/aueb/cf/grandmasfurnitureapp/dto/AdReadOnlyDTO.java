package gr.aueb.cf.grandmasfurnitureapp.dto;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for ad data retrieval operations.
 * Contains read-only ad information with nested category and city DTOs.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdReadOnlyDTO {

    private Long id;
    private String title;
    private CategoryReadOnlyDTO category;        // Use DTO instead of entity
    private CityReadOnlyDTO city;               // Use DTO instead of entity
    private Condition condition;
    private BigDecimal price;
    private Boolean isAvailable;
    private String description;
    private String imagePath;

    // Only the Ad's audit fields - no duplicates
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // User info (optional - add if you want to show who posted the ad)
    private String userFirstName;
    private String userLastName;
    private String userPhone;
}