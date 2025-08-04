package gr.aueb.cf.grandmasfurnitureapp.core.specifications;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import gr.aueb.cf.grandmasfurnitureapp.model.Ad;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.City;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class AdSpecification {

    private AdSpecification() {

    }

    public static Specification<Ad>  adUserEmailIs(String email) {
        return ((root, query, criteriaBuilder) -> {
            if (email == null || email.isBlank()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("email"), email);
        });
    }

    public static Specification<Ad> adIsAvailable(Boolean isAvailable) {
        return ((root, query, criteriaBuilder) -> {
            if (isAvailable == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("isAvailable"), isAvailable);
        });
    }

    public static Specification<Ad> adTitleLike(String title) {
        return ((root, query, criteriaBuilder) -> {
            if (title == null || title.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + title.toUpperCase() + "%");
        });
    }

    public static Specification<Ad> adDescriptionLike(String description) {
        return ((root, query, criteriaBuilder) -> {
            if (description == null || description.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + description.toUpperCase() + "%");
        });
    }

    public static Specification<Ad> adCategoryIs(Long categoryId) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryId == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, Category> category = root.join("category");
            return criteriaBuilder.equal(category.get("id"), categoryId);
        });
    }

    public static Specification<Ad> adCategoryNameLike(String categoryName) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, Category> category = root.join("category");
            return criteriaBuilder.like(criteriaBuilder.upper(category.get("category")), "%" + categoryName.toUpperCase() + "%");
        });
    }

    public static Specification<Ad> adConditionIs(Condition condition) {
        return ((root, query, criteriaBuilder) -> {
            if (condition == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            return criteriaBuilder.equal(root.get("condition"), condition);
        });
    }

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

    public static Specification<Ad> adCityIs(Long cityId) {
        return ((root, query, criteriaBuilder) -> {
            if (cityId == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, City> city = root.join("city");
            return criteriaBuilder.equal(city.get("id"), cityId);
        });
    }

    public static Specification<Ad> adCityNameLike(String cityName) {
        return ((root, query, criteriaBuilder) -> {
            if (cityName == null || cityName.trim().isEmpty()) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, City> city = root.join("city");
            return criteriaBuilder.like(criteriaBuilder.upper(city.get("cityName")), "%" + cityName.toUpperCase() + "%");
        });
    }

    public static Specification<Ad> adUserIs(Long userId) {
        return ((root, query, criteriaBuilder) -> {
            if (userId == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            Join<Ad, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("id"), userId);
        });
    }


}
