package gr.aueb.cf.grandmasfurnitureapp.model.static_data;


import gr.aueb.cf.grandmasfurnitureapp.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Category entity for furniture categorization.
 * Represents different types of furniture categories.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column ( unique = true, nullable = false)
    private String category;
}
