package gr.aueb.cf.model;


import gr.aueb.cf.core.enums.Condition;
import gr.aueb.cf.model.static_data.Category;
import gr.aueb.cf.model.static_data.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ads")
public class Ad extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    private BigDecimal price;

    private boolean isAvailable;

    private String description;
}
