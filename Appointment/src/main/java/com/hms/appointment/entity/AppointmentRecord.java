package com.hms.appointment.entity;

import com.hms.appointment.dto.AppointmentRecordDTO;
import com.hms.appointment.utility.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppointmentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long doctorId;
    private Long patientId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="appointment_id")
    private Appointment appointment;
    private String symptoms;
    private String diagnosis;
    private String referredBy;
    private String referredTo;
    private String tests;
    private String notes;
    private LocalDate followUpDate;
    private LocalDateTime createdAt;

    public AppointmentRecordDTO toDTO(){
        return new AppointmentRecordDTO(
                id,
                doctorId,
                patientId,
                appointment.getId(),
                StringListConverter.convertStringToList(symptoms),
                diagnosis,
                referredBy,
                referredTo,
                StringListConverter.convertStringToList(tests),
                notes,
                followUpDate,
                createdAt
        );
    }
}
