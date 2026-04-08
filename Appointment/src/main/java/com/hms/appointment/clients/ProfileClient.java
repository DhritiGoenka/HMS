package com.hms.appointment.clients;

import com.hms.appointment.config.FeignClientInterceptor;
import com.hms.appointment.dto.DoctorDTO;
import com.hms.appointment.dto.DoctorDropDown;
import com.hms.appointment.dto.PatientDTO;
import com.hms.appointment.exception.HmsException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ProfileMS", configuration = FeignClientInterceptor.class)
public interface ProfileClient {
    @GetMapping("/profile/doctor/exists/{id}")
    Boolean doctorExist(@PathVariable("id") Long id);

    @GetMapping("/profile/patient/exists/{id}")
    Boolean patientExist(@PathVariable("id") Long id);

    @GetMapping("/profile/patient/get/{id}")
    PatientDTO getPatientById(@PathVariable("id")Long id);

    @GetMapping("/profile/doctor/get/{id}")
    DoctorDTO getDoctorById(@PathVariable("id")Long id);

    @GetMapping("/profile/doctor/getDoctorById")
    List<DoctorDropDown> getDoctorsById(@RequestParam List<Long> ids) throws HmsException;
}
