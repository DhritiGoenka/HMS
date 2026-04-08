package com.hms.appointment.dto;

import com.hms.appointment.entity.Medicine;
import com.hms.appointment.entity.Prescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDTO {
    private Long id;
    private String name;
    private Long medicineId;
    private String dosage;
    private String frequency;
    private Integer duration; //in days
    private String route;
    private String type;
    private String instructions; //oral, intravenous etc
    private Long prescriptionId;

    public Medicine toEntity(){
        return new Medicine(
                id,
                name,
                medicineId,
                dosage,
                frequency,
                duration,
                route,
                type,
                instructions,
                new Prescription(prescriptionId)
        );
    }
}
