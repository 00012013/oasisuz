package uz.example.oasisuz.controller;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.example.oasisuz.dto.BannerDTO;
import uz.example.oasisuz.dto.CottageDTO;
import uz.example.oasisuz.dto.CottageListDTO;
import uz.example.oasisuz.dto.FilterDTO;
import uz.example.oasisuz.service.CottageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cottage/")
public class CottageController {

    private final CottageService cottageService;

    @GetMapping("get-all")
    public List<CottageDTO> getAllCottages() {
        return cottageService.getAllCottages();
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
    public List<CottageDTO> getCottagesById(@RequestBody CottageListDTO cottageListDTO) {
        return cottageService.getCottagesById(cottageListDTO);
    }

    @GetMapping("get/{id}")
    public CottageDTO getCottageById(@PathVariable Integer id) {
        return cottageService.getCottageById(id);
    }

    @PostMapping("filter")
    public List<CottageDTO> getCottageByFilter(@RequestBody FilterDTO filterDTO) {
        return cottageService.getCottageByFilter(filterDTO);
    }
}
