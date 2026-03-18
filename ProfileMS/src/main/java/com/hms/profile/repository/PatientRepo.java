package com.hms.profile.repository;

import com.hms.profile.dto.DoctorDropDown;
import com.hms.profile.dto.PatientDropDown;
import com.hms.profile.entity.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepo extends CrudRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByAadharNumber(String aadharNumber);

    @Query("SELECT p.id AS id, p.name AS name FROM Patient p")
    List<PatientDropDown> findAllPatientDropdowns();
}
