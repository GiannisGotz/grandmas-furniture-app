package gr.aueb.cf.grandmasfurnitureapp.repository;

import gr.aueb.cf.grandmasfurnitureapp.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {
}
