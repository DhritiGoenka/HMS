package com.hms.profile.repository;

import com.hms.profile.entity.Doctor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DoctorRepo extends CrudRepository<Doctor,Long> {
    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);
}
