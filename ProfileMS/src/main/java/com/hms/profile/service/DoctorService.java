package com.hms.profile.service;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.exception.HmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public interface DoctorService {
    public Long addDoctor(DoctorDTO doctorDTO) throws HmsException;
    public DoctorDTO getDoctorById(Long id) throws HmsException;

    public DoctorDTO updateDoctor(DoctorDTO doctorDTO) throws HmsException;
}
