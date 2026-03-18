package com.hms.appointment.service;

import com.hms.appointment.dto.AppointmentDTO;
import com.hms.appointment.dto.AppointmentRecordDTO;
import com.hms.appointment.entity.AppointmentRecord;
import com.hms.appointment.exception.HmsException;

public interface AppointmentRecordService {
    public Long createAppointmentRecord(AppointmentRecordDTO appointmentRecordDTO) throws HmsException;
    public void updateAppointmentRecord(AppointmentRecordDTO appointmentRecordDTO) throws HmsException;
    public AppointmentRecordDTO getRecordByAppointmentId(Long appointmentId) throws HmsException;
    public AppointmentRecordDTO getAppointmentRecordById(Long recordId) throws HmsException;

}
