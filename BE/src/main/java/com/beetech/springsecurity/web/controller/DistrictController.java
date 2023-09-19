package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.service.DistrictService;
import com.beetech.springsecurity.web.dto.response.District.DistrictDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/district")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DistrictController {
    private final DistrictService districtService;

    @GetMapping("/getAllDistrict")
    public ResponseEntity<List<DistrictDto>> getAllDistrict() {
        List<DistrictDto> districts = districtService.getAllDistricts();
        return ResponseEntity.ok(districts);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DistrictDto> getDistrictById(@PathVariable("id") int districtId) {
        DistrictDto district = districtService.getDistrictById(districtId);
        return ResponseEntity.ok(district);
    }
}
