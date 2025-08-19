package gr.aueb.cf.grandmasfurnitureapp.core.specifications;

import gr.aueb.cf.grandmasfurnitureapp.core.enums.Role;
import gr.aueb.cf.grandmasfurnitureapp.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private UserSpecification() {
        // Private constructor to prevent instantiation
    }

    /**
     * Filter users by username (case-insensitive like search)
     */
    public static Specification<User> userUsernameLike(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(
                    criteriaBuilder.upper(root.get("username")),
                    "%" + username.toUpperCase() + "%"
            );
        };
    }

    /**
     * Filter users by first name (case-insensitive like search)
     */
    public static Specification<User> userFirstNameLike(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(
                    criteriaBuilder.upper(root.get("firstName")),
                    "%" + firstName.toUpperCase() + "%"
            );
        };
    }

    /**
     * Filter users by last name (case-insensitive like search)
     */
    public static Specification<User> userLastNameLike(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(
                    criteriaBuilder.upper(root.get("lastName")),
                    "%" + lastName.toUpperCase() + "%"
            );
        };
    }

    /**
     * Filter users by email (case-insensitive like search)
     */
    public static Specification<User> userEmailLike(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(
                    criteriaBuilder.upper(root.get("email")),
                    "%" + email.toUpperCase() + "%"
            );
        };
    }

    /**
     * Filter users by exact email match (for your existing method)
     */
    public static Specification<User> userEmailIs(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    /**
     * Filter users by phone (case-insensitive like search)
     */
    public static Specification<User> userPhoneLike(String phone) {
        return (root, query, criteriaBuilder) -> {
            if (phone == null || phone.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(
                    criteriaBuilder.upper(root.get("phone")),
                    "%" + phone.toUpperCase() + "%"
            );
        };
    }

    /**
     * Filter users by role (exact match)
     */
    public static Specification<User> userRoleIs(Role role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    /**
     * Filter users by active status
     */
    public static Specification<User> userIsActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    /**
     * Filter users by exact username match
     */
    public static Specification<User> userUsernameEquals(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("username"), username);
        };
    }
}