package com.hms.appointment.service;

import com.hms.appointment.dto.MedicineDTO;

import java.util.List;

public interface MedicineService {
    public Long saveMedicine(MedicineDTO medicineDTO);
    public List<MedicineDTO> saveAllMedicines(List<MedicineDTO> medicineList);
    public List<MedicineDTO> getAllMedicinesByPrescriptionId(Long prescriptionId);
}
