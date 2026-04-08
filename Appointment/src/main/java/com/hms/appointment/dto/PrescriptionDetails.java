package com.hms.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDetails {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String doctorName;
    private String doctorEmail;
    private String doctorPhone;
    private Long appointmentId;
    private LocalDate prescriptionDate;
    private List<MedicineDTO> medicines;
    private String notes;
}
