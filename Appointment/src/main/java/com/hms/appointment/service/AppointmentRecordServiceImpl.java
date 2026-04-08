package com.hms.appointment.service;

import com.hms.appointment.clients.ProfileClient;
import com.hms.appointment.dto.AppointmentRecordDTO;
import com.hms.appointment.dto.DoctorDropDown;
import com.hms.appointment.dto.RecordDetailsDTO;
import com.hms.appointment.entity.AppointmentRecord;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.AppointmentRecordRepo;
import com.hms.appointment.utility.StringListConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentRecordServiceImpl implements AppointmentRecordService{
    private final AppointmentRecordRepo appointmentRecordRepo;
    private final PrescriptionService prescriptionService;
    private final ProfileClient profileClient;

    @Override
    public Long createAppointmentRecord(AppointmentRecordDTO appointmentRecordDTO) throws HmsException {
        Optional<AppointmentRecord> existingRecord = appointmentRecordRepo.findByAppointment_Id(appointmentRecordDTO.getAppointmentId());
        if(existingRecord.isPresent()){
            throw new HmsException("APPOINTMENT_RECORD_ALREADY_EXISTS");
        }
        appointmentRecordDTO.setCreatedAt(LocalDateTime.now());
        Long id =  appointmentRecordRepo.save(appointmentRecordDTO.toEntity()).getId();
        if(appointmentRecordDTO.getPrescription()!=null){
            appointmentRecordDTO.getPrescription().setAppointmentId(appointmentRecordDTO.getAppointmentId());
            prescriptionService.savePrescription(appointmentRecordDTO.getPrescription());
        }
        return id;
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
    public AppointmentRecordDTO getRecordDetailsByAppointmentId(Long appointmentId) throws HmsException {
        AppointmentRecordDTO record = appointmentRecordRepo.findByAppointment_Id(appointmentId).orElseThrow(()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND")).toDTO();
        record.setPrescription(prescriptionService.getPrescriptionByAppointmentId(appointmentId));
        return record;
    }

    @Override
    public AppointmentRecordDTO getAppointmentRecordById(Long recordId) throws HmsException {
        return appointmentRecordRepo.findById(recordId).orElseThrow(()->new HmsException("APPOINTMENT_RECORD_NOT_FOUND")).toDTO();
    }

    @Override
    public List<RecordDetailsDTO> getAppointmentRecordsByPatientId(Long patientId) throws HmsException {
        List<AppointmentRecord> records = appointmentRecordRepo.findByPatientId(patientId);
        List<RecordDetailsDTO> recordDetails = records.stream()
                .map(AppointmentRecord::toRecordDetailsDTO)
                .toList();
        List<Long> doctorIds = recordDetails.stream()
                .map(RecordDetailsDTO::getDoctorId)
                .distinct()
                .toList();
        List<DoctorDropDown> doctors = profileClient.getDoctorsById(doctorIds);
        Map<Long, DoctorDropDown> doctormap = doctors.stream()
                .collect(Collectors.toMap(DoctorDropDown::getId, doctor->doctor));
        recordDetails.forEach(record->{
            DoctorDropDown doctor = doctormap.get(record.getDoctorId());
            if(doctor!=null){
                record.setDoctorName(
                        doctor.getName() != null ? doctor.getName() : "Unknown Doctor"
                );

                record.setDoctorEmail(
                        doctor.getEmail() != null ? doctor.getEmail() : "N/A"
                );

                record.setDoctorPhone(
                        doctor.getPhone() != null ? doctor.getPhone() : "N/A"
                );
            }
            else {
                record.setDoctorName("Unknown Doctor");
                record.setDoctorEmail("N/A");
                record.setDoctorPhone("N/A");
            }
        });
        return recordDetails;
    }

    @Override
    public Boolean recordExists(Long appointmentId) throws HmsException {
        return appointmentRecordRepo.existsByAppointment_Id(appointmentId);
    }


}
