package com.agrios.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;

    private String firstName;
    private String lastName;
    private String fatherName;

    private String email;
    private String phoneNumber;

    // Location Details
    private String stateCode;
    private String districtCode;
    private String tehsilCode;
    private String blockCode;
    private String gramPanchayatCode;
    private String villageCode;

    private String role;

    private boolean enabled;
}