package com.hms.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDetailsDTO {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private String doctorEmail;
    private String doctorPhone;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private Long appointmentId;
    private List<String> symptoms;
    private String diagnosis;
    private String referredBy;
    private String referredTo;
    private List<String> tests;
    private String notes;
    private LocalDate followUpDate;
    private LocalDateTime createdAt;
}
