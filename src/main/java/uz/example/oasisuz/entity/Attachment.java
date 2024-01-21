package uz.example.oasisuz.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileOriginalName;

    private String contentType;

    private byte[] mainContent;

    private String name;

    @ManyToOne
    @JsonIgnore
    private Cottage cottage;

}
