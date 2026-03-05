package com.hms.appointment.service;

import com.hms.appointment.dto.*;
import com.hms.appointment.entity.Appointment;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private APIService apiService;

    @Override
    public Long scheduleAppointment(AppointmentDTO appointmentDTO) throws HmsException{
        Boolean doctorExists = apiService.doctorExist(appointmentDTO.getDoctorId()).block();
        if(doctorExists==null || !doctorExists){
            throw new HmsException("DOCTOR_NOT_FOUND");
        }

        Boolean patientExists = apiService.patientExist(appointmentDTO.getPatientId()).block();
        if(patientExists==null || !patientExists){
            throw new HmsException("PATIENT_NOT_FOUND");
        }

        appointmentDTO.setStatus(Status.SCHEDULED);
        return appointmentRepo.save(appointmentDTO.toEntity()).getId();
    }

    @Override
    public void cancelAppointment(Long appointmentId) throws HmsException{
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(()->new HmsException("APPOINTMENT_NOT_FOUND"));
        if(appointment.getStatus().equals(Status.CANCELLED)){
            throw new HmsException("APPOINTMENT_ALREADY_CANCELLED");
        }
        appointment.setStatus(Status.CANCELLED);
        appointmentRepo.save(appointment);
    }

    @Override
    public void completeAppointment(Long appointmentId) {

    }

    @Override
    public void rescheduleAppointment(Long appointmentId, String newDateTime) {

    }

    @Override
    public AppointmentDTO getAppointmentDetails(Long appointmentId) throws HmsException{
        return appointmentRepo.findById(appointmentId).orElseThrow(()->new HmsException("APPOINTMENT_NOT_FOUND")).toDTO();
    }

    @Override
    public AppointmentDetails getAppointmentDetailsWithName(Long appointmentId) throws HmsException{
        AppointmentDTO appointmentDTO = appointmentRepo.findById(appointmentId).orElseThrow(()->new HmsException("APPOINTMENT_NOT_FOUND")).toDTO();
        DoctorDTO doctorDTO = apiService.getDoctorById(appointmentDTO.getDoctorId()).block();
        PatientDTO patientDTO = apiService.getPatientById(appointmentDTO.getPatientId()).block();
        if(doctorDTO==null){
            throw new HmsException("DOCTOR_NOT_FOUND");
        }
        if(patientDTO==null){
            throw new HmsException("PATIENT_NOT_FOUND");
        }
        return new AppointmentDetails(
                appointmentDTO.getId(),
                appointmentDTO.getPatientId(),
                appointmentDTO.getDoctorId(),
                patientDTO.getName(),
                doctorDTO.getName(),
                patientDTO.getEmail(),
                doctorDTO.getEmail(),
                appointmentDTO.getAppointmentTime(),
                appointmentDTO.getStatus(),
                appointmentDTO.getReason(),
                appointmentDTO.getNotes()
        );
    }
}
