package com.agrios.user.repository;

import com.agrios.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check duplicate email
    boolean existsByEmail(String email);

    // Check duplicate phone number
    boolean existsByPhoneNumber(String phoneNumber);

    // Find farmers by district
    List<User> findByDistrictCode(String districtCode);

    // Find farmers by district and block
    List<User> findByDistrictCodeAndBlockCode(
            String districtCode,
            String blockCode
    );

    // Find farmers by district, block and village
    List<User> findByDistrictCodeAndBlockCodeAndVillageCode(
            String districtCode,
            String blockCode,
            String villageCode
    );
}