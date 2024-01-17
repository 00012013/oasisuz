package uz.example.oasisuz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.example.oasisuz.dto.CottageDTO;
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

    @PostMapping("add/{userId}")
    public CottageDTO addCottage(@RequestBody CottageDTO cottageDTO, @PathVariable() Integer userId) {
        return cottageService.addCottage(cottageDTO, userId);
    }

}
