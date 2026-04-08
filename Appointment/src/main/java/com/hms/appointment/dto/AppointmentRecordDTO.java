package com.hms.appointment.dto;

import com.hms.appointment.entity.Appointment;
import com.hms.appointment.entity.AppointmentRecord;
import com.hms.appointment.utility.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRecordDTO {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private Long appointmentId;
    private List<String> symptoms;
    private String diagnosis;
    private String referredBy;
    private String referredTo;
    private List<String> tests;
    private String notes;
    private LocalDate followUpDate;
    private LocalDateTime createdAt;
    private PrescriptionDTO prescription;

    public AppointmentRecord toEntity(){
        return new AppointmentRecord(
                id,
                doctorId,
                patientId,
                new Appointment(appointmentId),
                StringListConverter.convertListToString(symptoms),
                diagnosis,
                referredBy,
                referredTo,
                StringListConverter.convertListToString(tests),
                notes,
                followUpDate,
                createdAt
        );
    }
}
