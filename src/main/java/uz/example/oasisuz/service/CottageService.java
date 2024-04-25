package uz.example.oasisuz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.example.oasisuz.dto.response.BannerDTO;
import uz.example.oasisuz.dto.CottageDTO;
import uz.example.oasisuz.dto.request.CottageListDTO;
import uz.example.oasisuz.dto.request.FilterDTO;
import uz.example.oasisuz.entity.Cottage;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.entity.enums.Equipments;
import uz.example.oasisuz.entity.enums.RoleEnum;
import uz.example.oasisuz.entity.enums.Status;
import uz.example.oasisuz.exception.CustomException;
import uz.example.oasisuz.repository.CottageRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CottageService {
    private final CottageRepository cottageRepository;
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    public List<CottageDTO> getAllCottages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Cottage> cottageList = cottageRepository.findAllByStatusOrderById(Status.APPROVED, pageable);
        return cottageList.stream().map(cottage -> modelMapper.map(cottage, CottageDTO.class)).toList();
    }

    public List<BannerDTO> getBannerCottages() {
        List<Cottage> fistThree = cottageRepository.getFistThree();
        return fistThree.stream().map(cottage -> modelMapper.map(cottage, BannerDTO.class)).toList();
    }

    public CottageDTO addCottage(CottageDTO cottageDTO, Integer userId) {
        Users user = usersService.getUser(userId);

        Cottage cottage = new Cottage();
        cottage.setName(cottageDTO.getName());
        cottage.setDescription(cottageDTO.getDescription());
        cottage.setLatitude(cottageDTO.getLatitude());
        cottage.setLongitude(cottageDTO.getLongitude());
        cottage.setEquipmentsList(cottageDTO.getEquipmentsList());
        cottage.setGuestCount(cottageDTO.getGuestCount());
        cottage.setTotalRoomCount(cottageDTO.getTotalRoomCount());
        cottage.setWeekDaysPrice(cottageDTO.getWeekDaysPrice());
        cottage.setWeekendDaysPrice(cottageDTO.getWeekendDaysPrice());
        cottage.setStatus(Status.PENDING);
        cottage.setUsers(user);

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
        Cottage cottage = getCottage(cottageId);
        return modelMapper.map(cottage, CottageDTO.class);
    }

    public void saveMainAttachmentId(Cottage cottage) {
        cottageRepository.save(cottage);
    }

    public List<CottageDTO> searchByName(String name) {
        List<Cottage> cottageList = cottageRepository.searchByName(name);

        return cottageList.stream().map(cottage -> modelMapper.map(cottage, CottageDTO.class)).toList();
    }

    public List<CottageDTO> getCottagesByIds(CottageListDTO cottageListDTO) {

        List<Cottage> cottageList = cottageRepository.findAllById(cottageListDTO.getCottageList());

        return cottageList.stream().map(cottage -> modelMapper.map(cottage, CottageDTO.class)).collect(toList());
    }

    public List<CottageDTO> getCottageByFilter(FilterDTO filterDTO) {

        if (filterDTO.getEquipmentList().length == 0) {
            filterDTO.setEquipmentList(null);
        }

        List<Cottage> cottageList = cottageRepository.filterCottage(filterDTO.getEquipmentList(), filterDTO.getSortBy(), filterDTO.getMaxPrice(), filterDTO.getMinPrice(), filterDTO.getName());

        return cottageList.stream().map(cottage -> modelMapper.map(cottage, CottageDTO.class)).collect(toList());
    }

    public List<CottageDTO> getPendingCottages(Integer userId, int page, int size) {

        Users user = usersService.getUser(userId);

        if (user.getRoles().get(0).getRoleEnum() != RoleEnum.ADMIN) {
            throw new CustomException("", HttpStatus.FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(page, size);

        List<Cottage> allByStatus = cottageRepository.findAllByStatusOrderById(Status.PENDING, pageable);

        return allByStatus.stream().map(cottage -> modelMapper.map(cottage, CottageDTO.class)).collect(toList());
    }

    public void changeCottageStatus(Integer userId, CottageDTO cottageDTO) {
        Users user = usersService.getUser(userId);

        if (user.getRoles().get(0).getRoleEnum() != RoleEnum.ADMIN) {
            throw new CustomException("", HttpStatus.FORBIDDEN);
        }
        Cottage cottage = getCottage(cottageDTO.getId());

        cottage.setStatus(cottageDTO.getStatus());

        cottageRepository.save(cottage);
    }

    public List<CottageDTO> getUserCottages(Integer userId) {
        Users user = usersService.getUser(userId);

        List<Cottage> cottageList = cottageRepository.findAllByUsersOrderById(user);

        return cottageList.stream().map(cottage -> modelMapper.map(cottage, CottageDTO.class)).toList();
    }

    public void updateCottage(CottageDTO cottageDTO, Integer userId) {
        Users user = usersService.getUser(userId);

        Cottage cottage = getCottage(cottageDTO.getId());

        if (!user.getId().equals(cottage.getUsers().getId())) {
            throw new CustomException("Failed to update. User id mismatch!", HttpStatus.BAD_REQUEST);
        }

        cottage.setName(cottageDTO.getName() != null ? cottageDTO.getName() : cottage.getName());
        cottage.setDescription(cottageDTO.getDescription() != null ? cottageDTO.getDescription() : cottage.getDescription());
        cottage.setWeekDaysPrice(cottageDTO.getWeekDaysPrice() != null ? cottageDTO.getWeekDaysPrice() : cottage.getWeekDaysPrice());
        cottage.setWeekendDaysPrice(cottageDTO.getWeekendDaysPrice() != null ? cottageDTO.getWeekendDaysPrice() : cottage.getWeekendDaysPrice());
        cottage.setTotalRoomCount(cottageDTO.getTotalRoomCount() != null ? cottageDTO.getTotalRoomCount() : cottage.getTotalRoomCount());
        cottage.setLatitude(cottageDTO.getLatitude() != null ? cottageDTO.getLatitude() : cottage.getLatitude());
        cottage.setLongitude(cottageDTO.getLongitude() != null ? cottageDTO.getLongitude() : cottage.getLongitude());
        cottage.setEquipmentsList(cottageDTO.getEquipmentsList() != null ? cottageDTO.getEquipmentsList() : cottage.getEquipmentsList());


        cottageRepository.save(cottage);
    }
}