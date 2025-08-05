package gr.aueb.cf.grandmasfurnitureapp.dto;


import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import gr.aueb.cf.grandmasfurnitureapp.model.static_data.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdReadOnlyDTO {

    private Long id;

    private String title;

    private Category category;

    private Condition condition;

    private BigDecimal price;

    private Boolean isAvailable;

    private String description;

    private String imagePath;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
