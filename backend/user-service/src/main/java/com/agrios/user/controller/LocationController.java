package com.agrios.user.controller;

import com.agrios.user.dto.response.BlockResponse;
import com.agrios.user.dto.response.DistrictResponse;
import com.agrios.user.dto.response.TehsilResponse;
import com.agrios.user.dto.response.VillageResponse;
import com.agrios.user.service.LocationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    // 1. Get all UP districts
    @GetMapping("/districts")
    public ResponseEntity<List<DistrictResponse>> getDistricts() {
        return ResponseEntity.ok(locationService.getDistricts());
    }

    // 2. Get tehsils by district code (e.g. ?districtCode=118)
    @GetMapping("/tehsils")
    public ResponseEntity<List<TehsilResponse>> getTehsils(@RequestParam String districtCode) {
        return ResponseEntity.ok(locationService.getTehsilsByDistrict(districtCode));
    }

    // 3. Get blocks by district code (e.g. ?districtCode=118)
    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponse>> getBlocks(@RequestParam String districtCode) {
        return ResponseEntity.ok(locationService.getBlocksByDistrict(districtCode));
    }

    // 4. Get villages by tehsil code (e.g. ?tehsilCode=766)
    @GetMapping("/villages")
    public ResponseEntity<List<VillageResponse>> getVillages(@RequestParam String tehsilCode) {
        return ResponseEntity.ok(locationService.getVillagesByTehsil(tehsilCode));
    }
}

