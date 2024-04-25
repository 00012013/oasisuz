package uz.example.oasisuz.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class FilterDTO {
    Double minPrice;
    Double maxPrice;
    String sortBy;
    String[] equipmentList;
    String name;
}
