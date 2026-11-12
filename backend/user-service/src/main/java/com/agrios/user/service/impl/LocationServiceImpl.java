package com.agrios.user.service.impl;

import com.agrios.user.dto.response.BlockResponse;
import com.agrios.user.dto.response.DistrictResponse;
import com.agrios.user.dto.response.TehsilResponse;
import com.agrios.user.dto.response.VillageResponse;
import com.agrios.user.service.LocationService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    private final List<DistrictResponse> districts = new ArrayList<>();
    private final Map<String, List<TehsilResponse>> tehsilsByDistrict = new HashMap<>();
    private final Map<String, List<BlockResponse>> blocksByDistrict = new HashMap<>();
    private final Map<String, List<VillageResponse>> villagesByTehsil = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info("Loading UP location data from CSV files...");
        loadDistricts();
        loadTehsils();
        loadBlocks();
        loadVillages();
        log.info("Loaded {} districts, {} tehsils, {} blocks, and villages into memory.",
                districts.size(), tehsilsByDistrict.size(), blocksByDistrict.size());
    }

    private void loadDistricts() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/up/up_districts.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    districts.add(new DistrictResponse(clean(parts[0]), clean(parts[1])));
                }
            }
        } catch (Exception e) {
            log.error("Failed to load UP districts: {}", e.getMessage());
        }
    }

    private void loadTehsils() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/up/up_tehsils.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String districtCode = clean(parts[0]);
                    tehsilsByDistrict.computeIfAbsent(districtCode, k -> new ArrayList<>())
                            .add(new TehsilResponse(districtCode, clean(parts[1]), clean(parts[2])));
                }
            }
        } catch (Exception e) {
            log.error("Failed to load UP tehsils: {}", e.getMessage());
        }
    }

    private void loadBlocks() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/up/up_blocks.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String districtCode = clean(parts[0]);
                    blocksByDistrict.computeIfAbsent(districtCode, k -> new ArrayList<>())
                            .add(new BlockResponse(districtCode, clean(parts[1]), clean(parts[2])));
                }
            }
        } catch (Exception e) {
            log.error("Failed to load UP blocks: {}", e.getMessage());
        }
    }

    private void loadVillages() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("data/up/up_villages.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String tehsilCode = clean(parts[1]);
                    villagesByTehsil.computeIfAbsent(tehsilCode, k -> new ArrayList<>())
                            .add(new VillageResponse(clean(parts[0]), tehsilCode, clean(parts[2]), clean(parts[3])));
                }
            }
        } catch (Exception e) {
            log.error("Failed to load UP villages: {}", e.getMessage());
        }
    }

    private String clean(String text) {
        return text != null ? text.replace("\"", "").trim() : "";
    }

    @Override
    public List<DistrictResponse> getDistricts() {
        return districts;
    }

    @Override
    public List<TehsilResponse> getTehsilsByDistrict(String districtCode) {
        return tehsilsByDistrict.getOrDefault(districtCode, Collections.emptyList());
    }

    @Override
    public List<BlockResponse> getBlocksByDistrict(String districtCode) {
        return blocksByDistrict.getOrDefault(districtCode, Collections.emptyList());
    }

    @Override
    public List<VillageResponse> getVillagesByTehsil(String tehsilCode) {
        return villagesByTehsil.getOrDefault(tehsilCode, Collections.emptyList());
    }
}

