package gr.aueb.cf.grandmasfurnitureapp.specification;

import gr.aueb.cf.grandmasfurnitureapp.model.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA Specifications for User entity filtering.
 * Provides reusable query criteria for user search operations.
 */
public class UserSpecification {

    public static Specification<User> userEmailIs(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    public static Specification<User> userIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }
}