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
    private final List<TehsilResponse> allTehsils = new ArrayList<>();
    private final Map<String, List<BlockResponse>> blocksByDistrict = new HashMap<>();
    private final List<BlockResponse> allBlocks = new ArrayList<>();
    private final Map<String, List<VillageResponse>> villagesByTehsil = new HashMap<>();
    private final List<VillageResponse> allVillages = new ArrayList<>();

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
                    TehsilResponse tehsil = new TehsilResponse(districtCode, clean(parts[1]), clean(parts[2]));
                    tehsilsByDistrict.computeIfAbsent(districtCode, k -> new ArrayList<>()).add(tehsil);
                    allTehsils.add(tehsil);
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
                    BlockResponse block = new BlockResponse(districtCode, clean(parts[1]), clean(parts[2]));
                    blocksByDistrict.computeIfAbsent(districtCode, k -> new ArrayList<>()).add(block);
                    allBlocks.add(block);
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
                    VillageResponse village = new VillageResponse(clean(parts[0]), tehsilCode, clean(parts[2]), clean(parts[3]));
                    villagesByTehsil.computeIfAbsent(tehsilCode, k -> new ArrayList<>()).add(village);
                    allVillages.add(village);
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
    public List<DistrictResponse> searchDistricts(String name, int limit) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String query = name.trim().toLowerCase();
        int maxResults = limit > 0 ? Math.min(limit, 100) : 20;

        return districts.stream()
                .filter(d -> d.getName().toLowerCase().contains(query))
                .limit(maxResults)
                .toList();
    }

    @Override
    public List<TehsilResponse> getTehsilsByDistrict(String districtCode) {
        return tehsilsByDistrict.getOrDefault(districtCode, Collections.emptyList());
    }

    @Override
    public List<TehsilResponse> searchTehsils(String name, String districtCode, int limit) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String query = name.trim().toLowerCase();
        int maxResults = limit > 0 ? Math.min(limit, 100) : 20;

        List<TehsilResponse> source = (districtCode != null && !districtCode.trim().isEmpty())
                ? tehsilsByDistrict.getOrDefault(districtCode.trim(), Collections.emptyList())
                : allTehsils;

        return source.stream()
                .filter(t -> t.getName().toLowerCase().contains(query))
                .limit(maxResults)
                .toList();
    }

    @Override
    public List<BlockResponse> getBlocksByDistrict(String districtCode) {
        return blocksByDistrict.getOrDefault(districtCode, Collections.emptyList());
    }

    @Override
    public List<BlockResponse> searchBlocks(String name, String districtCode, int limit) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String query = name.trim().toLowerCase();
        int maxResults = limit > 0 ? Math.min(limit, 100) : 20;

        List<BlockResponse> source = (districtCode != null && !districtCode.trim().isEmpty())
                ? blocksByDistrict.getOrDefault(districtCode.trim(), Collections.emptyList())
                : allBlocks;

        return source.stream()
                .filter(b -> b.getName().toLowerCase().contains(query))
                .limit(maxResults)
                .toList();
    }

    @Override
    public List<VillageResponse> getVillagesByTehsil(String tehsilCode) {
        return villagesByTehsil.getOrDefault(tehsilCode, Collections.emptyList());
    }

    @Override
    public List<VillageResponse> searchVillages(String name, String tehsilCode, int limit) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String query = name.trim().toLowerCase();
        int maxResults = limit > 0 ? Math.min(limit, 100) : 20;

        List<VillageResponse> source = (tehsilCode != null && !tehsilCode.trim().isEmpty())
                ? villagesByTehsil.getOrDefault(tehsilCode.trim(), Collections.emptyList())
                : allVillages;

        return source.stream()
                .filter(v -> v.getName().toLowerCase().contains(query))
                .limit(maxResults)
                .toList();
    }
}

