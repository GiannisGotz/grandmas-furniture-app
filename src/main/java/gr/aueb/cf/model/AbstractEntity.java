package gr.aueb.cf.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity {

    @CreatedDate
    @Column( name= "created_at", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name ="updated_at", nullable = false)
    private LocalDateTime updatedDate;
}
