package gr.aueb.cf.grandmasfurnitureapp.core.filters;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.data.domain.Sort;

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

    @Nullable
    private Boolean myAds;
    
    // Add pagination and sorting fields to make them available in the builder
    @Nullable
    private Integer page;
    
    @Nullable
    private Integer pageSize;
    
    @Nullable
    private String sortBy;
    
    @Nullable
    private String sortDirection;
    
    // Override parent class methods to use our fields
    @Override
    public int getPage() {
        return page != null ? Math.max(page, 0) : super.getPage();
    }
    
    @Override
    public int getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : super.getPageSize();
    }
    
    @Override
    public String getSortBy() {
        return sortBy != null && !sortBy.isBlank() ? sortBy : super.getSortBy();
    }
    
    @Override
    public Sort.Direction getSortDirection() {
        if (sortDirection != null && !sortDirection.trim().isEmpty()) {
            try {
                return Sort.Direction.fromString(sortDirection.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return Sort.Direction.ASC;
            }
        }
        return super.getSortDirection();
    }
}
