package com.agrios.user.service;

import com.agrios.user.dto.response.BlockResponse;
import com.agrios.user.dto.response.DistrictResponse;
import com.agrios.user.dto.response.TehsilResponse;
import com.agrios.user.dto.response.VillageResponse;

import java.util.List;

public interface LocationService {

    List<DistrictResponse> getDistricts();

    List<DistrictResponse> searchDistricts(String name, int limit);

    List<TehsilResponse> getTehsilsByDistrict(String districtCode);

    List<TehsilResponse> searchTehsils(String name, String districtCode, int limit);

    List<BlockResponse> getBlocksByDistrict(String districtCode);

    List<BlockResponse> searchBlocks(String name, String districtCode, int limit);

    List<VillageResponse> getVillagesByTehsil(String tehsilCode);

    List<VillageResponse> searchVillages(String name, String tehsilCode, int limit);
}

