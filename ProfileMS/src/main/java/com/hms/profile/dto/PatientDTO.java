package com.hms.profile.dto;

import com.hms.profile.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    private String phone;
    private String address;
    private String aadharNumber;
    private BloodGroup bloodGroup;
    private String allergies;
    private String chronicDisease;

    public Patient toEntity(){
        return new Patient(
                this.id,
                this.name,
                this.email,
                this.dob,
                this.phone,
                this.address,
                this.aadharNumber,
                this.bloodGroup,
                this.allergies,
                this.chronicDisease
        );
    }

}
