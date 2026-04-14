package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
import com.hms.pharmacy.entity.MedicineInventory;
import com.hms.pharmacy.entity.StockStatus;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.MedicineInventoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicineInventoryServiceImpl implements MedicineInventoryService{
    private final MedicineInventoryRepo medicineInventoryRepo;
    private final MedicineService medicineService;

    private void markExpired(List<MedicineInventory> inventories){
        for(MedicineInventory inventory : inventories){
            inventory.setStatus(StockStatus.EXPIRED);
        }
        medicineInventoryRepo.saveAll(inventories);
    }
    
    @Override
    public List<MedicineInventoryDTO> getAllMedicines() throws HmsException {
        List<MedicineInventory> inventories = (List<MedicineInventory>) medicineInventoryRepo.findAll();
        return inventories.stream().map(MedicineInventory::toDTO).toList();
    }

    @Override
    public MedicineInventoryDTO getMedicineById(Long id) throws HmsException {
        return medicineInventoryRepo.findById(id).orElseThrow(()->new HmsException("INVENTORY_NOT_FOUND")).toDTO();
    }

    @Override
    public Long addMedicine(MedicineInventoryDTO medicineInventoryDTO) throws HmsException {
        medicineInventoryDTO.setAddedDate(LocalDate.now());
        medicineInventoryDTO.setInitialQuantity(medicineInventoryDTO.getQuantity());
        medicineInventoryDTO.setStatus(StockStatus.ACTIVE);
        medicineService.addStock(medicineInventoryDTO.getId(),medicineInventoryDTO.getQuantity());
        return medicineInventoryRepo.save(medicineInventoryDTO.toEntity()).toDTO().getId();
    }

    @Override
    public void deleteMedicine(Long id) throws HmsException {
        medicineInventoryRepo.deleteById(id);
    }

    @Override
    public MedicineInventoryDTO updateMedicine(MedicineInventoryDTO medicineInventoryDTO) throws HmsException {
        MedicineInventory existing =  medicineInventoryRepo.findById(medicineInventoryDTO.getId()).orElseThrow(()->new HmsException("INVENTORY_NOT_FOUND"));
        existing.setBatchNo(medicineInventoryDTO.getBatchNo());
        if(existing.getQuantity()< medicineInventoryDTO.getQuantity()){
            medicineService.addStock(medicineInventoryDTO.getMedicineId(), medicineInventoryDTO.getQuantity() - existing.getQuantity());
        }
        else if(existing.getQuantity() > medicineInventoryDTO.getQuantity()){
            medicineService.removeStock(medicineInventoryDTO.getMedicineId(), medicineInventoryDTO.getQuantity() - existing.getQuantity());
        }
        existing.setQuantity(medicineInventoryDTO.getQuantity());
        existing.setInitialQuantity(medicineInventoryDTO.getQuantity());
        existing.setExpiryDate(medicineInventoryDTO.getExpiryDate());
        return medicineInventoryRepo.save(existing).toDTO();
    }

    @Override
    @Scheduled(cron = "0 30 14 * * ?")
    public void deleteExpiredMedicines() throws HmsException {
        List<MedicineInventory> expiredMedicines = medicineInventoryRepo.findByExpiryDateBefore(LocalDate.now());
        for(MedicineInventory medicine : expiredMedicines){
            medicineService.removeStock(medicine.getMedicine().getId(), medicine.getQuantity());
        }
        markExpired(expiredMedicines);
    }



}
