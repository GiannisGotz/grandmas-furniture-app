package gr.aueb.cf.grandmasfurnitureapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for standardized API response messages.
 * Used for error responses and success confirmations.
 */
@Data
@AllArgsConstructor
public class ResponseMessageDTO {
    private String code;
    private String description;

    public ResponseMessageDTO(String code) {
        this.code = code;
        this.description = "";
    }
}
