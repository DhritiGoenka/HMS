package com.hms.profile.entity;

import com.hms.profile.dto.BloodGroup;
import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.PatientDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private LocalDate dob;
    private String phone;
    private String address;
    @Column(unique = true)
    private String licenseNumber;
    private String specialisation;
    private String department;
    private Integer totalExperience;

    public DoctorDTO toDTO(){
        return new DoctorDTO(
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
