package com.agrios.user.service;

import com.agrios.user.dto.response.BlockResponse;
import com.agrios.user.dto.response.DistrictResponse;
import com.agrios.user.dto.response.TehsilResponse;
import com.agrios.user.dto.response.VillageResponse;

import java.util.List;

public interface LocationService {

    List<DistrictResponse> getDistricts();

    List<TehsilResponse> getTehsilsByDistrict(String districtCode);

    List<BlockResponse> getBlocksByDistrict(String districtCode);

    List<VillageResponse> getVillagesByTehsil(String tehsilCode);
}

