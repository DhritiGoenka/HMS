package com.hms.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDropDown {
    private Long id;
    private String name;
    private String email;
    private String phone;
}