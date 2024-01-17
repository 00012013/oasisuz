package uz.example.oasisuz.dto;

import lombok.Data;

@Data
public class AttachmentDTO {
    private Integer id;

    private String fileOriginalName;

    private String contentType;

    private byte[] mainContent;
}
