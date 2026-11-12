package com.agrios.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    // Personal Details
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Father name is required")
    private String fatherName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    // Location Details
    @NotBlank(message = "State is required")
    private String stateCode;

    @NotBlank(message = "District is required")
    private String districtCode;

    @NotBlank(message = "Tehsil is required")
    private String tehsilCode;

    @NotBlank(message = "Block is required")
    private String blockCode;

    @NotBlank(message = "Gram Panchayat is required")
    private String gramPanchayatCode;

    @NotBlank(message = "Village is required")
    private String villageCode;
}