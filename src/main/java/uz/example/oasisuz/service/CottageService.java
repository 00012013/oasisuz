package uz.example.oasisuz.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.example.oasisuz.dto.CottageDTO;
import uz.example.oasisuz.entity.Cottage;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.repository.CottageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CottageService {
    private final CottageRepository cottageRepository;
    private final ModelMapper modelMapper;
    private final UsersService usersService;
    public List<CottageDTO> getAllCottages(){
        List<Cottage> cottageList = cottageRepository.findAll();
        return cottageList.stream().map(cottage ->  modelMapper.map(cottage, CottageDTO.class)).toList();
    }

    public CottageDTO addCottage(CottageDTO cottageDTO, Integer userId){
        Users user = usersService.getUser(userId);
        if(user == null){
            // to do
        }

        Cottage cottage = new Cottage();
        cottage.setName(cottageDTO.getName());
        cottage.setDescription(cottageDTO.getDescription());
        cottage.setLatitude(cottageDTO.getLatitude());
        cottage.setLongitude(cottageDTO.getLongitude());
        cottage.setBookedDates(cottageDTO.getBookedDates());
        cottage.setEquipmentsList(cottageDTO.getEquipmentsEnumList());
        cottage.setGuestCount(cottageDTO.getGuestCount());
        cottage.setTotalRoomCount(cottageDTO.getTotalRoomCount());
        cottage.setWeekDaysPrice(cottageDTO.getWeekDaysPrice());
        cottage.setWeekendDaysPrice(cottageDTO.getWeekendDaysPrice());
        cottage.setUsers(user);

        Cottage save = cottageRepository.save(cottage);
        return modelMapper.map(save, CottageDTO.class);
    }
}
