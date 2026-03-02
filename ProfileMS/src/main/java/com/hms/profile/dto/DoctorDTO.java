package com.hms.profile.dto;

import com.hms.profile.entity.Doctor;
import com.hms.profile.entity.Patient;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    private String phone;
    private String address;
    private String licenseNumber;
    private String specialisation;
    private String department;
    private Integer totalExperience;

    public Doctor toEntity(){
        return new Doctor(
                this.id,
                this.name,
                this.email,
                this.dob,
                this.phone,
                this.address,
                this.licenseNumber,
                this.specialisation,
                this.department,
                this.totalExperience
        );
    }
}
