package gr.aueb.cf.grandmasfurnitureapp.core.filters;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import jakarta.annotation.Nullable;
import lombok.*;

import java.math.BigDecimal;

/**
 * Filter criteria for ad search operations.
 * Contains optional filters for title, category, price range, location, and availability.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AdFilters extends GenericFilters {

    @Nullable
    private String title;

    @Nullable
    private Long categoryId;

    @Nullable
    private String categoryName;

    @Nullable
    private Condition condition;

    @Nullable
    private BigDecimal minPrice;

    @Nullable
    private BigDecimal maxPrice;

    @Nullable
    private Long cityId;

    @Nullable
    private String cityName;

    @Nullable
    private Long userId;

    @Nullable
    private Boolean isAvailable;

    @Nullable
    private String description;
}
