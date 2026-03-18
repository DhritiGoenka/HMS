package com.hms.profile.api;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropDown;
import com.hms.profile.dto.PatientDTO;
import com.hms.profile.dto.PatientDropDown;
import com.hms.profile.exception.HmsException;
import com.hms.profile.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/profile/patient")
public class PatientAPI {

    @Autowired
    private PatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<Long> addPatient(@RequestBody PatientDTO patientDTO) throws HmsException {
        return new ResponseEntity<>(patientService.addPatient(patientDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(patientService.getPatientById(id),HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientDTO patientDTO) throws HmsException{
        return new ResponseEntity<>(patientService.updatePatient(patientDTO),HttpStatus.OK);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> patientExists(@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(patientService.patientExists(id), HttpStatus.OK);
    }

    @GetMapping("/dropdowns")
    public ResponseEntity<List<PatientDropDown>> getPatientDropdowns() throws HmsException{
        return new ResponseEntity<>(patientService.getPatientDropdowns(), HttpStatus.OK);
    }

}
