package gr.aueb.cf.grandmasfurnitureapp.repository;

import gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for Category entity operations.
 * Provides CRUD operations for furniture categories.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByCategory(String category);
}