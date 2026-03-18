package com.hms.appointment.api;

import com.hms.appointment.dto.AppointmentRecordDTO;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.service.AppointmentRecordService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/appointment-record")
@Validated
public class AppointmentRecordAPI {
    @Autowired
    private AppointmentRecordService appointmentRecordService;

    @PostMapping("/create")
    public ResponseEntity<Long> createAppointmentRecord(@RequestBody AppointmentRecordDTO appointmentRecordDTO) throws HmsException {
        return new ResponseEntity<>(appointmentRecordService.createAppointmentRecord(appointmentRecordDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAppointmentRecord(@RequestBody AppointmentRecordDTO appointmentRecordDTO) throws HmsException{
        appointmentRecordService.updateAppointmentRecord(appointmentRecordDTO);
        return new ResponseEntity<>("Appointment Record Updated successfully",HttpStatus.OK);
    }

    @GetMapping("/getByAppointmentId/{appointmentId}")
    public ResponseEntity<AppointmentRecordDTO> getAppointmentRecordByAppointmentId(@PathVariable Long appointmentId) throws HmsException{
        return new ResponseEntity<>(appointmentRecordService.getRecordByAppointmentId(appointmentId),HttpStatus.OK);
    }

    @GetMapping("/getById/{recordId}")
    public ResponseEntity<AppointmentRecordDTO> getAppointmentRecordById(@PathVariable Long recordId) throws HmsException{
        return new ResponseEntity<>(appointmentRecordService.getAppointmentRecordById(recordId),HttpStatus.OK);
    }
}
