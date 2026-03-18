package com.hms.profile.service;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropDown;
import com.hms.profile.dto.PatientDTO;
import com.hms.profile.dto.PatientDropDown;
import com.hms.profile.exception.HmsException;
import com.hms.profile.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService{

    @Autowired
    private PatientRepo patientRepo;

    @Override
    public Long addPatient(PatientDTO patientDTO) throws HmsException {
        if(patientDTO.getEmail()!=null && patientRepo.findByEmail(patientDTO.getEmail()).isPresent()){
            throw new HmsException("PATIENT_ALREADY_EXISTS");
        }
        if(patientDTO.getAadharNumber()!=null && patientRepo.findByAadharNumber(patientDTO.getAadharNumber()).isPresent()){
            throw new HmsException("PATIENT_ALREADY_EXISTS");
        }
        return patientRepo.save(patientDTO.toEntity()).getId();
    }

    @Override
    public PatientDTO getPatientById(Long id) throws HmsException {
        return patientRepo.findById(id).orElseThrow(()->new HmsException("PATIENT_NOT_FOUND")).toDTO();
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patientDTO) throws HmsException {
        patientRepo.findById(patientDTO.getId()).orElseThrow(()->new HmsException("PATIENT_NOT_FOUND"));
        return patientRepo.save(patientDTO.toEntity()).toDTO();
    }

    @Override
    public Boolean patientExists(Long id) throws HmsException{
        return patientRepo.existsById(id);
    }

    @Override
    public List<PatientDropDown> getPatientDropdowns() throws HmsException {
        return patientRepo.findAllPatientDropdowns();
    }
}
