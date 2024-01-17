package uz.example.oasisuz.dto;

import lombok.Data;
import uz.example.oasisuz.entity.enums.Equipments;


import java.time.LocalDate;
import java.util.List;

@Data
public class CottageDTO {

    private Integer id;

    private String name;

    private String description;

    private Double weekDaysPrice;

    private Double weekendDaysPrice;

    private Double latitude;

    private Double longitude;

    private Integer guestCount;

    private Integer totalRoomCount;

    private List<String> equipmentsList;

    private List<LocalDate> bookedDates;

    private List<AttachmentDTO> attachmentsList;

    private AttachmentDTO mainAttachment;

    public List<Equipments> getEquipmentsEnumList() {
        return equipmentsList.stream()
                .map(Equipments::valueOf)
                .toList();
    }
}
