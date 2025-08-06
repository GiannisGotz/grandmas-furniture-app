package gr.aueb.cf.grandmasfurnitureapp.repository;

import gr.aueb.cf.grandmasfurnitureapp.model.static_data.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for City entity operations.
 * Provides CRUD operations for city data.
 */
public interface CityRepository extends JpaRepository<City, Long> {
    
    Optional<City> findByCityName(String cityName);

}