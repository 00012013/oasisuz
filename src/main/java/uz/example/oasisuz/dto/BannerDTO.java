package uz.example.oasisuz.dto;

import lombok.Data;


@Data
public class BannerDTO {
    private Integer id;

    private String name;

    private Double weekDaysPrice;

    private Double weekendDaysPrice;

    private AttachmentDTO mainAttachment;
}
