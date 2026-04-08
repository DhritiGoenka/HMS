package com.hms.profile.repository;

import com.hms.profile.dto.DoctorDropDown;
import com.hms.profile.entity.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepo extends CrudRepository<Doctor,Long> {
    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    @Query("SELECT d.id AS id, d.name AS name FROM Doctor d")
    List<DoctorDropDown> findAllDoctorDropdowns();

    @Query("SELECT d.id AS id, d.name AS name FROM Doctor d WHERE d.id IN ?1")
    List<DoctorDropDown> findAllDoctorDropdownsByIds(List<Long> ids);
}
