package com.agrios.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VillageResponse {
    private String districtCode;
    private String tehsilCode;
    private String code;
    private String name;
}

