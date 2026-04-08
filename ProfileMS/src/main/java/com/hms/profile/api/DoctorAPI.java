package com.hms.profile.api;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropDown;
import com.hms.profile.entity.Doctor;
import com.hms.profile.exception.HmsException;
import com.hms.profile.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/profile/doctor")
public class DoctorAPI {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/add")
    public ResponseEntity<Long> addDoctor(@RequestBody DoctorDTO doctorDTO) throws HmsException {
        return new ResponseEntity<>(doctorService.addDoctor(doctorDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(doctorService.getDoctorById(id),HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<DoctorDTO> updatePatient(@RequestBody DoctorDTO doctorDTO) throws HmsException{
        return new ResponseEntity<>(doctorService.updateDoctor(doctorDTO),HttpStatus.OK);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> doctorExists(@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(doctorService.doctorExists(id), HttpStatus.OK);
    }

    @GetMapping("/dropdowns")
    public ResponseEntity<List<DoctorDropDown>> getDoctorDropdowns() throws HmsException{
        return new ResponseEntity<>(doctorService.getDoctorDropdowns(), HttpStatus.OK);
    }

    @GetMapping("/getDoctorById")
    public ResponseEntity<List<DoctorDropDown>> getDoctorsById(@RequestParam List<Long> ids) throws HmsException{
        return new ResponseEntity<>(doctorService.getDoctorDropdownsById(ids), HttpStatus.OK);
    }

}
