package com.hms.appointment.service;

import com.hms.appointment.dto.AppointmentDTO;
import com.hms.appointment.dto.AppointmentRecordDTO;
import com.hms.appointment.dto.RecordDetailsDTO;
import com.hms.appointment.entity.AppointmentRecord;
import com.hms.appointment.exception.HmsException;

import java.util.List;

public interface AppointmentRecordService {
    public Long createAppointmentRecord(AppointmentRecordDTO appointmentRecordDTO) throws HmsException;
    public void updateAppointmentRecord(AppointmentRecordDTO appointmentRecordDTO) throws HmsException;
    public AppointmentRecordDTO getRecordByAppointmentId(Long appointmentId) throws HmsException;
    public AppointmentRecordDTO getRecordDetailsByAppointmentId(Long appointmentId) throws HmsException;
    public AppointmentRecordDTO getAppointmentRecordById(Long recordId) throws HmsException;
    public List<RecordDetailsDTO> getAppointmentRecordsByPatientId(Long patientId) throws HmsException;
    public Boolean recordExists(Long appointmentId) throws HmsException;
}
