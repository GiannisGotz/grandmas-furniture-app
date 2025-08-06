package gr.aueb.cf.grandmasfurnitureapp.model.static_data;

import gr.aueb.cf.grandmasfurnitureapp.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * City entity for location data.
 * Represents cities where furniture ads can be posted.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cities")
public class City extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(unique = true, nullable = false)
    private String cityName;

}
