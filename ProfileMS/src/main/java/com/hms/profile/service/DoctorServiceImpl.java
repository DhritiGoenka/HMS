package com.hms.profile.service;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropDown;
import com.hms.profile.entity.Doctor;
import com.hms.profile.exception.HmsException;
import com.hms.profile.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService{

    @Autowired
    private DoctorRepo doctorRepo;

    @Override
    public Long addDoctor(DoctorDTO doctorDTO) throws HmsException {
        if(doctorDTO.getEmail()!=null && doctorRepo.findByEmail(doctorDTO.getEmail()).isPresent()){
            throw new HmsException("DOCTOR_ALREADY_EXISTS");
        }
        if(doctorDTO.getLicenseNumber()!=null && doctorRepo.findByLicenseNumber(doctorDTO.getLicenseNumber()).isPresent()){
            throw new HmsException("DOCTOR_ALREADY_EXISTS");
        }
        return doctorRepo.save(doctorDTO.toEntity()).getId();
    }

    @Override
    public DoctorDTO getDoctorById(Long id) throws HmsException {
        return doctorRepo.findById(id).orElseThrow(()->new HmsException("DOCTOR_NOT_FOUND")).toDTO();
    }

    @Override
    public DoctorDTO updateDoctor(DoctorDTO doctorDTO) throws HmsException {
        doctorRepo.findById(doctorDTO.getId()).orElseThrow(()->new HmsException("DOCTOR_NOT_FOUND"));
        return doctorRepo.save(doctorDTO.toEntity()).toDTO();

    }

    @Override
    public Boolean doctorExists(Long id) throws HmsException{
        return doctorRepo.existsById(id);
    }

    @Override
    public List<DoctorDropDown> getDoctorDropdowns() throws HmsException {
        return doctorRepo.findAllDoctorDropdowns();
    }
}
