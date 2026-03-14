package com.hms.appointment.clients;

import com.hms.appointment.config.FeignClientInterceptor;
import com.hms.appointment.dto.DoctorDTO;
import com.hms.appointment.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
