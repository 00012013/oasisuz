package uz.example.oasisuz.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.example.oasisuz.dto.response.BannerDTO;
import uz.example.oasisuz.dto.CottageDTO;
import uz.example.oasisuz.dto.request.CottageListDTO;
import uz.example.oasisuz.dto.request.FilterDTO;
import uz.example.oasisuz.service.CottageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cottage/")
public class CottageController {

    private final CottageService cottageService;

    @GetMapping("get-all/")
    public List<CottageDTO> getAllCottages(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size) {
        return cottageService.getAllCottages(page, size);
    }

    @GetMapping("get-banner")
    public ResponseEntity<List<BannerDTO>> getBannerCottages() {
        return ResponseEntity.ok(cottageService.getBannerCottages());
    }

    @PostMapping("add/{userId}")
    public CottageDTO addCottage(@RequestBody CottageDTO cottageDTO, @PathVariable Integer userId) {
        return cottageService.addCottage(cottageDTO, userId);
    }

    @GetMapping(value = "search/{name}")
    public List<CottageDTO> searchByName(@PathVariable String name) {
        return cottageService.searchByName(name);
    }

    @PostMapping("get/cottage-list")
    public List<CottageDTO> getCottagesByIds(@RequestBody CottageListDTO cottageListDTO) {
        return cottageService.getCottagesByIds(cottageListDTO);
    }

    @GetMapping("get/{id}")
    public CottageDTO getCottageById(@PathVariable Integer id) {
        return cottageService.getCottageById(id);
    }

    @PostMapping("filter")
    public List<CottageDTO> getCottageByFilter(@RequestBody FilterDTO filterDTO) {
        return cottageService.getCottageByFilter(filterDTO);
    }

    @GetMapping("get-pending/{userId}")
    public List<CottageDTO> getPendingCottages(@PathVariable Integer userId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return cottageService.getPendingCottages(userId, page, size);
    }

    @PostMapping("change-status/{userId}")
    public void changeCottageStatus(@PathVariable Integer userId, @RequestBody CottageDTO cottageDTO) {
        cottageService.changeCottageStatus(userId, cottageDTO);
    }

    @GetMapping("get/user-cottages/{userId}")
    public List<CottageDTO> getUserCottages(@PathVariable Integer userId){
        return cottageService.getUserCottages(userId);
    }

    @PostMapping("update/{userId}")
    public void updateCottage(@RequestBody CottageDTO cottageDTO, @PathVariable Integer userId){
        cottageService.updateCottage(cottageDTO, userId);
    }

}
