package gr.aueb.cf.grandmasfurnitureapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Attachment entity for file storage metadata.
 * Manages file information and relationships to ads.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "attachments")
public class Attachment extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "saved_name")
    private String savedName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "content_type")
    private String contentType;
    
    private String extension;

    @OneToOne(mappedBy = "image")
    private Ad ad;
}
