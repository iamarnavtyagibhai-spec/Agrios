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

    // 2. Search districts by name (e.g. ?name=Varanasi)
    @GetMapping("/districts/search")
    public ResponseEntity<List<DistrictResponse>> searchDistricts(
            @RequestParam String name,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(locationService.searchDistricts(name, limit));
    }

    // 3. Get tehsils by district code (e.g. ?districtCode=118)
    @GetMapping("/tehsils")
    public ResponseEntity<List<TehsilResponse>> getTehsils(@RequestParam String districtCode) {
        return ResponseEntity.ok(locationService.getTehsilsByDistrict(districtCode));
    }

    // 4. Search tehsils by name (e.g. ?name=Pindra or ?name=Pin&districtCode=187)
    @GetMapping("/tehsils/search")
    public ResponseEntity<List<TehsilResponse>> searchTehsils(
            @RequestParam String name,
            @RequestParam(required = false) String districtCode,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(locationService.searchTehsils(name, districtCode, limit));
    }

    // 5. Get blocks by district code (e.g. ?districtCode=118)
    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponse>> getBlocks(@RequestParam String districtCode) {
        return ResponseEntity.ok(locationService.getBlocksByDistrict(districtCode));
    }

    // 6. Search blocks by name (e.g. ?name=Achhnera or ?name=Ach&districtCode=118)
    @GetMapping("/blocks/search")
    public ResponseEntity<List<BlockResponse>> searchBlocks(
            @RequestParam String name,
            @RequestParam(required = false) String districtCode,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(locationService.searchBlocks(name, districtCode, limit));
    }

    // 7. Get villages by tehsil code (e.g. ?tehsilCode=766)
    @GetMapping("/villages")
    public ResponseEntity<List<VillageResponse>> getVillages(@RequestParam String tehsilCode) {
        return ResponseEntity.ok(locationService.getVillagesByTehsil(tehsilCode));
    }

    // 8. Search villages by name (e.g. ?name=Rampur or ?name=Ram&tehsilCode=766)
    @GetMapping("/villages/search")
    public ResponseEntity<List<VillageResponse>> searchVillages(
            @RequestParam String name,
            @RequestParam(required = false) String tehsilCode,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(locationService.searchVillages(name, tehsilCode, limit));
    }
}

