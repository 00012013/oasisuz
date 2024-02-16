package uz.example.oasisuz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.example.oasisuz.dto.BannerDTO;
import uz.example.oasisuz.dto.CottageDTO;
import uz.example.oasisuz.dto.CottageListDTO;
import uz.example.oasisuz.dto.FilterDTO;
import uz.example.oasisuz.entity.Cottage;
import uz.example.oasisuz.entity.enums.Equipments;
import uz.example.oasisuz.exception.CustomException;
import uz.example.oasisuz.mapper.BannerMapper;
import uz.example.oasisuz.mapper.CottageMapper;
import uz.example.oasisuz.repository.CottageRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CottageService {
    private final CottageRepository cottageRepository;
    private final UsersService usersService;
    private final CottageMapper cottageMapper;
    private final BannerMapper bannerMapper;

    public List<CottageDTO> getAllCottages() {
        List<Cottage> cottageList = cottageRepository.findAll();
        return cottageList.stream().map(cottageMapper::toCottageDto).toList();
    }

    public List<BannerDTO> getBannerCottages() {
        List<Cottage> fistThree = cottageRepository.getFistThree();
        return fistThree.stream().map(bannerMapper::cottageToBannerDto).toList();
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
        return cottageMapper.toCottageDto(save);
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
        return cottageMapper.toCottageDto(cottage);
    }

    public void saveMainAttachmentId(Cottage cottage) {
        cottageRepository.save(cottage);
    }

    public List<String> searchByName(String name) {
        return cottageRepository.searchByName(name);
    }

    public List<CottageDTO> getCottagesById(CottageListDTO cottageListDTO) {

        List<Cottage> cottageList = cottageRepository.findAllById(cottageListDTO.getCottageList());

        return cottageList.stream().map(cottageMapper::toCottageDto).collect(toList());
    }

    public List<CottageDTO> getCottageByFilter(FilterDTO filterDTO) {

        List<Cottage> cottageList = cottageRepository.filterCottage(filterDTO.getEquipmentList(), filterDTO.getSortBy(), filterDTO.getMaxPrice(), filterDTO.getMinPrice(), filterDTO.getName());

        return  cottageList.stream().map(cottageMapper::toCottageDto).collect(toList());
    }
}
