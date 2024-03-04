package uz.example.oasisuz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converters;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.example.oasisuz.dto.BannerDTO;
import uz.example.oasisuz.dto.CottageDTO;
import uz.example.oasisuz.dto.CottageListDTO;
import uz.example.oasisuz.dto.FilterDTO;
import uz.example.oasisuz.entity.Cottage;
import uz.example.oasisuz.entity.enums.Equipments;
import uz.example.oasisuz.exception.CustomException;
import uz.example.oasisuz.repository.CottageRepository;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CottageService {
    private final CottageRepository cottageRepository;
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    public List<CottageDTO> getAllCottages() {
        List<Cottage> cottageList = cottageRepository.findAll();
        return cottageList.stream().map(cottage -> modelMapper.map(cottage,CottageDTO.class)).toList();
    }

    public List<BannerDTO> getBannerCottages() {
        List<Cottage> fistThree = cottageRepository.getFistThree();
        return fistThree.stream().map(cottage -> modelMapper.map(cottage,BannerDTO.class)).toList();
    }

    public CottageDTO addCottage(CottageDTO cottageDTO, Integer userId) {
//        Users user = usersService.getUser(userId);
//        if (user == null) {
//            // to do
//        }

        Cottage cottage = new Cottage();
        cottage.setName(cottageDTO.getName());
        cottage.setDescription(cottageDTO.getDescription());
        cottage.setLatitude(cottageDTO.getLatitude());
        cottage.setLongitude(cottageDTO.getLongitude());
//        cottage.setBookedDates(cottageDTO.getBookedDates());
        cottage.setEquipmentsList(cottageDTO.getEquipmentsList());
        cottage.setGuestCount(cottageDTO.getGuestCount());
        cottage.setTotalRoomCount(cottageDTO.getTotalRoomCount());
        cottage.setWeekDaysPrice(cottageDTO.getWeekDaysPrice());
        cottage.setWeekendDaysPrice(cottageDTO.getWeekendDaysPrice());
//        cottage.setUsers(user);

        Cottage save = cottageRepository.save(cottage);
        return modelMapper.map(save, CottageDTO.class);
    }

    private List<Equipments> getEquipmentsEnumList(List<String> equipmentsList) {
        System.out.println(Equipments.FOOTBALL_PITCH);
        return equipmentsList.stream()
                .map(Equipments::valueOf)
                .toList();
    }

    public Cottage getCottage(Integer cottageId) {
        return cottageRepository.findById(cottageId).orElseThrow(() -> new CustomException(String.format("Cottage not found by id %s", cottageId), HttpStatus.NOT_FOUND));
    }

    public CottageDTO getCottageById(Integer cottageId) {
//        log.trace("get Cottage by Id controller");
        Cottage cottage = getCottage(cottageId);
        return modelMapper.map(cottage,CottageDTO.class);
    }

    public void saveMainAttachmentId(Cottage cottage) {
        cottageRepository.save(cottage);
    }

    public List<String> searchByName(String name) {
        return cottageRepository.searchByName(name);
    }

    public List<CottageDTO> getCottagesById(CottageListDTO cottageListDTO) {

        List<Cottage> cottageList = cottageRepository.findAllById(cottageListDTO.getCottageList());

        return cottageList.stream().map(cottage -> modelMapper.map(cottage,CottageDTO.class)).collect(toList());
    }

    public List<CottageDTO> getCottageByFilter(FilterDTO filterDTO) {

        if(filterDTO.getEquipmentList().length == 0){
            filterDTO.setEquipmentList(null);
        }

        List<Cottage> cottageList = cottageRepository.filterCottage(filterDTO.getEquipmentList(), filterDTO.getSortBy(), filterDTO.getMaxPrice(), filterDTO.getMinPrice(), filterDTO.getName());

        return  cottageList.stream().map(cottage -> modelMapper.map(cottage,CottageDTO.class)).collect(toList());
    }
}
