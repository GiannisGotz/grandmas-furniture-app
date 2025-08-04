package gr.aueb.cf.grandmasfurnitureapp.core.filters;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

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
    private Pageable pageable;
}
