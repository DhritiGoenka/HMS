package com.hms.appointment.api;

import com.hms.appointment.dto.AppointmentRecordDTO;
import com.hms.appointment.dto.PrescriptionDetails;
import com.hms.appointment.dto.RecordDetailsDTO;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.service.AppointmentRecordService;
import com.hms.appointment.service.PrescriptionService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/appointment/record")
@Validated
@RequiredArgsConstructor
public class AppointmentRecordAPI {
    @Autowired
    private AppointmentRecordService appointmentRecordService;
    private final PrescriptionService prescriptionService;

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

    @GetMapping("/getDetailsByAppointmentId/{appointmentId}")
    public ResponseEntity<AppointmentRecordDTO> getDetailsAppointmentRecordByAppointmentId(@PathVariable Long appointmentId) throws HmsException{
        return new ResponseEntity<>(appointmentRecordService.getRecordDetailsByAppointmentId(appointmentId),HttpStatus.OK);
    }

    @GetMapping("/getById/{recordId}")
    public ResponseEntity<AppointmentRecordDTO> getAppointmentRecordById(@PathVariable Long recordId) throws HmsException{
        return new ResponseEntity<>(appointmentRecordService.getAppointmentRecordById(recordId),HttpStatus.OK);
    }

    @GetMapping("/getRecordsByPatientId/{patientId}")
    public ResponseEntity<List<RecordDetailsDTO>> getRecordsByPatientId(@PathVariable Long patientId) throws HmsException{
        return new ResponseEntity<>(appointmentRecordService.getAppointmentRecordsByPatientId(patientId), HttpStatus.OK);
    }

    @GetMapping("/recordExists/{appointmentId}")
    public ResponseEntity<Boolean> recordExists(@PathVariable Long appointmentId) throws HmsException{
        return new ResponseEntity<>(appointmentRecordService.recordExists(appointmentId), HttpStatus.OK);
    }

    @GetMapping("/getPrescriptionsByPatientId/{patientId}")
    public ResponseEntity<List<PrescriptionDetails>> getPrescriptionsByPatientId(@PathVariable Long patientId) throws HmsException{
        return new ResponseEntity<>(prescriptionService.getPrescriptionsByPatientId(patientId), HttpStatus.OK);
    }

}
