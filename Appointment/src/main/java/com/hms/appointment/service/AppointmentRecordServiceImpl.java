package com.hms.appointment.service;

import com.hms.appointment.dto.AppointmentRecordDTO;
import com.hms.appointment.entity.AppointmentRecord;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.AppointmentRecordRepo;
import com.hms.appointment.utility.StringListConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentRecordServiceImpl implements AppointmentRecordService{
    private final AppointmentRecordRepo appointmentRecordRepo;

    @Override
    public Long createAppointmentRecord(AppointmentRecordDTO appointmentRecordDTO) throws HmsException {
        Optional<AppointmentRecord> existingRecord = appointmentRecordRepo.findByAppointment_Id(appointmentRecordDTO.getAppointmentId());
        if(existingRecord.isPresent()){
            throw new HmsException("APPOINTMENT_RECORD_ALREADY_EXISTS");
        }
        return appointmentRecordRepo.save(appointmentRecordDTO.toEntity()).getId();
    }

    @Override
    public void updateAppointmentRecord(AppointmentRecordDTO appointmentRecordDTO) throws HmsException {
        AppointmentRecord existing = appointmentRecordRepo.findById(appointmentRecordDTO.getId()).orElseThrow(()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND"));
        existing.setNotes(appointmentRecordDTO.getNotes());
        existing.setDiagnosis(appointmentRecordDTO.getDiagnosis());
        existing.setFollowUpDate(appointmentRecordDTO.getFollowUpDate());
        existing.setSymptoms(StringListConverter.convertListToString(appointmentRecordDTO.getSymptoms()));
        existing.setTests(StringListConverter.convertListToString(appointmentRecordDTO.getTests()));
        existing.setReferredBy(appointmentRecordDTO.getReferredBy());
        existing.setReferredTo(appointmentRecordDTO.getReferredTo());
        appointmentRecordRepo.save(existing);
    }

    @Override
    public AppointmentRecordDTO getRecordByAppointmentId(Long appointmentId) throws HmsException {
        return appointmentRecordRepo.findByAppointment_Id(appointmentId).orElseThrow(()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND")).toDTO();

    }

    @Override
    public AppointmentRecordDTO getAppointmentRecordById(Long recordId) throws HmsException {
        return appointmentRecordRepo.findById(recordId).orElseThrow(()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND")).toDTO();
    }
}
