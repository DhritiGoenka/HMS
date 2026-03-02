package com.hms.profile.repository;

import com.hms.profile.entity.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatientRepo extends CrudRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByAadharNumber(String aadharNumber);
}
