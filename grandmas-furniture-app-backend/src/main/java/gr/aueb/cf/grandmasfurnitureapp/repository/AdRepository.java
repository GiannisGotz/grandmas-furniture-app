package gr.aueb.cf.grandmasfurnitureapp.repository;

import gr.aueb.cf.grandmasfurnitureapp.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Ad entity operations.
 * Provides CRUD operations and custom queries for furniture advertisements.
 */
public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

    Optional<Ad> findByCityCityName(String cityName);
    Optional<Ad> findByPrice(BigDecimal price);
    List<Ad> findByIsAvailableTrue();
    List<Ad> findByUserId(Long userId);

}
