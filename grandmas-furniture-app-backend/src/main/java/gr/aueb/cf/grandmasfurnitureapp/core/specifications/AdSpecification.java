package gr.aueb.cf.grandmasfurnitureapp.core.specifications;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import gr.aueb.cf.grandmasfurnitureapp.model.Ad;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.City;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * JPA Specifications for Ad entity filtering.
 * Provides reusable query criteria for dynamic ad search operations.
 * 
 * <p>This class contains static methods that return JPA Specifications for filtering ads
 * based on various criteria such as title, category, price, location, and availability.
 * All specifications handle null values gracefully by returning a "no filter" condition.</p>
 * 
 * <p>Usage example:</p>
 * <pre>{@code
 * Specification<Ad> spec = AdSpecification.adTitleLike("vintage")
 *     .and(AdSpecification.adPriceBetween(BigDecimal.valueOf(50), BigDecimal.valueOf(500)))
 *     .and(AdSpecification.adIsAvailable(true));
 * }</pre>
 * 
 * @author Giannis Gotzaridis
 */
public class AdSpecification {

    /**
     * Private constructor to prevent instantiation.
     * This class only contains static methods.
     */
    private AdSpecification() {
        // Utility class - no instances needed
    }

    /**
     * Creates a specification to filter ads by user email.
     * 
     * @param email The email address to filter by
     * @return Specification that filters ads by user email, or no filter if email is null/blank
     */
    public static Specification<Ad> adUserEmailIs(String email) {
        return ((root, query, criteriaBuilder) -> {
            if (email == null || email.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("email"), email);
        });
    }

    /**
     * Creates a specification to filter ads by availability status.
     * 
     * @param isAvailable The availability status to filter by (true for available, false for unavailable)
     * @return Specification that filters ads by availability, or no filter if isAvailable is null
     */
    public static Specification<Ad> adIsAvailable(Boolean isAvailable) {
        return ((root, query, criteriaBuilder) -> {
            if (isAvailable == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("isAvailable"), isAvailable);
        });
    }

    /**
     * Creates a specification to filter ads by title using case-insensitive LIKE search.
     * 
     * @param title The title text to search for (partial matches supported)
     * @return Specification that filters ads by title, or no filter if title is null/blank
     */
    public static Specification<Ad> adTitleLike(String title) {
        return ((root, query, criteriaBuilder) -> {
            if (title == null || title.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + title.toUpperCase() + "%");
        });
    }

    /**
     * Creates a specification to filter ads by description using case-insensitive LIKE search.
     * 
     * @param description The description text to search for (partial matches supported)
     * @return Specification that filters ads by description, or no filter if description is null/blank
     */
    public static Specification<Ad> adDescriptionLike(String description) {
        return ((root, query, criteriaBuilder) -> {
            if (description == null || description.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + description.toUpperCase() + "%");
        });
    }

    /**
     * Creates a specification to filter ads by category ID.
     * 
     * @param categoryId The category ID to filter by
     * @return Specification that filters ads by category ID, or no filter if categoryId is null
     */
    public static Specification<Ad> adCategoryIs(Long categoryId) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryId == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, Category> category = root.join("category");
            return criteriaBuilder.equal(category.get("id"), categoryId);
        });
    }

    /**
     * Creates a specification to filter ads by category name using case-insensitive LIKE search.
     * 
     * @param categoryName The category name to search for (partial matches supported)
     * @return Specification that filters ads by category name, or no filter if categoryName is null/blank
     */
    public static Specification<Ad> adCategoryNameLike(String categoryName) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, Category> category = root.join("category");
            return criteriaBuilder.like(criteriaBuilder.upper(category.get("category")), "%" + categoryName.toUpperCase() + "%");
        });
    }

    /**
     * Creates a specification to filter ads by condition.
     * 
     * @param condition The condition enum value to filter by
     * @return Specification that filters ads by condition, or no filter if condition is null
     */
    public static Specification<Ad> adConditionIs(Condition condition) {
        return ((root, query, criteriaBuilder) -> {
            if (condition == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("condition"), condition);
        });
    }

    /**
     * Creates a specification to filter ads by price range.
     * Supports filtering by minimum price, maximum price, or both.
     * 
     * @param minPrice The minimum price (inclusive), or null for no lower bound
     * @param maxPrice The maximum price (inclusive), or null for no upper bound
     * @return Specification that filters ads by price range, or no filter if both prices are null
     */
    public static Specification<Ad> adPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return ((root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
        });
    }

    /**
     * Creates a specification to filter ads by city ID.
     * 
     * @param cityId The city ID to filter by
     * @return Specification that filters ads by city ID, or no filter if cityId is null
     */
    public static Specification<Ad> adCityIs(Long cityId) {
        return ((root, query, criteriaBuilder) -> {
            if (cityId == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, City> city = root.join("city");
            return criteriaBuilder.equal(city.get("id"), cityId);
        });
    }

    /**
     * Creates a specification to filter ads by city name using case-insensitive LIKE search.
     * 
     * @param cityName The city name to search for (partial matches supported)
     * @return Specification that filters ads by city name, or no filter if cityName is null/blank
     */
    public static Specification<Ad> adCityNameLike(String cityName) {
        return ((root, query, criteriaBuilder) -> {
            if (cityName == null || cityName.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, City> city = root.join("city");
            return criteriaBuilder.like(criteriaBuilder.upper(city.get("cityName")), "%" + cityName.toUpperCase() + "%");
        });
    }

    /**
     * Creates a specification to filter ads by user ID.
     * 
     * @param userId The user ID to filter by
     * @return Specification that filters ads by user ID, or no filter if userId is null
     */
    public static Specification<Ad> adUserIs(Long userId) {
        return ((root, query, criteriaBuilder) -> {
            if (userId == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("id"), userId);
        });
    }

    /**
     * Creates a specification to filter ads by current user ownership.
     * When myAds is true, filters ads to show only those belonging to the current user.
     * 
     * @param myAds Whether to filter by current user's ads (true) or show all ads (false)
     * @param currentUserId The ID of the currently authenticated user
     * @return Specification that filters ads by user ownership, or no filter if myAds is false or currentUserId is null
     */
    public static Specification<Ad> adIsMyAds(Boolean myAds, Long currentUserId) {
        return ((root, query, criteriaBuilder) -> {
            if (myAds == null || !myAds || currentUserId == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<Ad, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("id"), currentUserId);
        });
    }
}
