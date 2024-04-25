package uz.example.oasisuz.dto.response;

import lombok.Data;
import uz.example.oasisuz.dto.AttachmentDTO;


@Data
public class BannerDTO {
    private Integer id;

    private String name;

    private Double weekDaysPrice;

    private Double weekendDaysPrice;

    private AttachmentDTO mainAttachment;
}
