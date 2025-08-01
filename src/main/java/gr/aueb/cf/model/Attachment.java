package gr.aueb.cf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "attachment")
public class Attachment extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Column(name = "saved_name")
    private String savedName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "content_type")
    private String contentType;
    private String extension;
}
