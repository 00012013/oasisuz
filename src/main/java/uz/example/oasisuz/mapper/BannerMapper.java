package uz.example.oasisuz.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import uz.example.oasisuz.dto.BannerDTO;
import uz.example.oasisuz.entity.Cottage;

@Component
@Mapper(componentModel = "spring")
public interface BannerMapper {
    BannerDTO cottageToBannerDto(Cottage cottage);
}
