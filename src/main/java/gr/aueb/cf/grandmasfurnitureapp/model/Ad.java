package gr.aueb.cf.grandmasfurnitureapp.model;


import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.City;
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

    @Column(nullable = false)
    private String title;


    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    private BigDecimal price;

    @Column(name = "is_available")
    private boolean isAvailable;

    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id")
    private Attachment image;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
