package uz.example.oasisuz.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.example.oasisuz.entity.enums.Equipments;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Enumerated(EnumType.STRING)
    private List<Equipments> equipmentsList;

    @ElementCollection
    private List<LocalDate> bookedDates;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.EAGER)
    private List<Attachment> attachmentsList;

    @OneToOne
    private Attachment mainAttachment;

    @ManyToOne()
    private Users users;
}
