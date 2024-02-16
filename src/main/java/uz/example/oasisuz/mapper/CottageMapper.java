package uz.example.oasisuz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import uz.example.oasisuz.dto.CottageDTO;
import uz.example.oasisuz.entity.Cottage;

@Component
@Mapper(componentModel = "spring")
public interface CottageMapper {

    @Mapping(source = "id", target = "id")
    CottageDTO toCottageDto(Cottage cottage);
}
