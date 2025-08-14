package gr.aueb.cf.grandmasfurnitureapp.dto;


import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdInsertDTO {

    @NotNull(message = "Title is required.")
    @NotBlank(message = "Title cannot be empty.")  // Also checks for empty strings
    @Size(min = 2, max = 30, message = "Title must be between 2 and 30 characters.")
    private String title;

    @NotNull(message = "Category name is required.")
    @NotBlank(message = "Category name cannot be empty.")
    private String categoryName;

    @NotNull(message = "City name is required.")
    @NotBlank(message = "City name cannot be empty.")
    private String cityName;

    @NotNull(message = "Condition is required.")
    private Condition condition;

    @NotNull(message= "Price is required.")
    private BigDecimal price;

    @NotNull(message= "Availability is required.")
    private Boolean isAvailable;

    @NotNull(message= "Description is required.")
    @NotBlank(message = "Description cannot be empty.")  // Also checks for empty strings
    @Size(min = 2, max = 100, message = "Description must be between 2 and 100 characters.")
    private String description;
}
