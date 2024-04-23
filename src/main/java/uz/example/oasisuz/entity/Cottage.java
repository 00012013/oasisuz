package uz.example.oasisuz.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.example.oasisuz.entity.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cottage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private Double weekDaysPrice;

    private Double weekendDaysPrice;

    private Double latitude;

    private Double longitude;

    private Integer guestCount;

    private Integer totalRoomCount;

    private String[] equipmentsList;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ElementCollection
    private List<LocalDate> bookedDates;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cottage", fetch = FetchType.LAZY)
    private List<Attachment> attachmentsList;

    private Integer mainAttachmentId;

    @ManyToOne()
    private Users users;

    private String contacts;
}
