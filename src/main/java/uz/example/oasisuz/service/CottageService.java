package uz.example.oasisuz.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.example.oasisuz.dto.BannerDTO;
import uz.example.oasisuz.dto.CottageDTO;
import uz.example.oasisuz.entity.Cottage;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.entity.enums.Equipments;
import uz.example.oasisuz.repository.CottageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CottageService {
    private final CottageRepository cottageRepository;
    private final ModelMapper modelMapper;
    private final UsersService usersService;

    public List<CottageDTO> getAllCottages() {
        List<Cottage> cottageList = cottageRepository.findAll();
        return cottageList.stream().map(cottage -> modelMapper.map(cottage, CottageDTO.class)).toList();
    }

    public List<BannerDTO> getBannerCottages() {
        List<Cottage> fistThree = cottageRepository.getFistThree();
        return fistThree.stream().map(cottage -> modelMapper.map(cottage, BannerDTO.class)).toList();
    }

    public CottageDTO addCottage(CottageDTO cottageDTO, Integer userId) {
        Users user = usersService.getUser(userId);
        if (user == null) {
            // to do
        }

        Cottage cottage = new Cottage();
        cottage.setName(cottageDTO.getName());
        cottage.setDescription(cottageDTO.getDescription());
        cottage.setLatitude(cottageDTO.getLatitude());
        cottage.setLongitude(cottageDTO.getLongitude());
//        cottage.setBookedDates(cottageDTO.getBookedDates());
        cottage.setEquipmentsList(getEquipmentsEnumList(cottageDTO.getEquipmentsList()));
        cottage.setGuestCount(cottageDTO.getGuestCount());
        cottage.setTotalRoomCount(cottageDTO.getTotalRoomCount());
        cottage.setWeekDaysPrice(cottageDTO.getWeekDaysPrice());
        cottage.setWeekendDaysPrice(cottageDTO.getWeekendDaysPrice());
        cottage.setUsers(user);

        Cottage save = cottageRepository.save(cottage);
        return modelMapper.map(save, CottageDTO.class);
    }

    private List<Equipments> getEquipmentsEnumList(List<String> equipmentsList) {
        return equipmentsList.stream()
                .map(Equipments::valueOf)
                .toList();
    }

    public Cottage getCottage(Integer cottageId) {
        return cottageRepository.findById(cottageId).orElse(null);
    }

    public void saveMainAttachmentId(Cottage cottage) {
        cottageRepository.save(cottage);
    }
}
