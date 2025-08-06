package gr.aueb.cf.grandmasfurnitureapp.dto;


import gr.aueb.cf.grandmasfurnitureapp.core.enums.Condition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * DTO for ad update operations.
 * Contains partial ad data for updating existing advertisements.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdUpdateDTO {

    private BigDecimal price;

    private Condition condition;

    private long cityId;

    private String description;

    private boolean isAvailable;

}

