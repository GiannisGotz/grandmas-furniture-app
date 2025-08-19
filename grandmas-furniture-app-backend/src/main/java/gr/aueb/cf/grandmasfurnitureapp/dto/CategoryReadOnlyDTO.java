package gr.aueb.cf.grandmasfurnitureapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for category data retrieval operations.
 * Contains read-only category information for API responses.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryReadOnlyDTO {
    private Long id;
    private String category;
}