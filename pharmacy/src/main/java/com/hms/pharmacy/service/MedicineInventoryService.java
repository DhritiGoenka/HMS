package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
import com.hms.pharmacy.exception.HmsException;

import java.util.List;

public interface MedicineInventoryService {
    public List<MedicineInventoryDTO> getAllMedicines() throws HmsException;
    public MedicineInventoryDTO getMedicineById(Long id) throws HmsException;
    public Long addMedicine(MedicineInventoryDTO medicineInventoryDTO) throws HmsException;
    public void deleteMedicine(Long id) throws HmsException;
    public MedicineInventoryDTO updateMedicine(MedicineInventoryDTO medicineInventoryDTO) throws HmsException;
    public void deleteExpiredMedicines() throws HmsException;
}
