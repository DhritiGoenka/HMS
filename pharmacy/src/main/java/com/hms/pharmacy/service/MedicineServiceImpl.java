package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.MedicineDTO;
import com.hms.pharmacy.entity.Medicine;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.MedicineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService{

    private final MedicineRepo medicineRepo;

    @Override
    public Long addMedicine(MedicineDTO medicineDTO) throws HmsException {
        Optional<Medicine> optional = medicineRepo.findByNameIgnoreCaseAndDosageIgnoreCase(medicineDTO.getName(), medicineDTO.getDosage());
        if(optional.isPresent()){
            throw new HmsException("MEDICINE_ALREADY_EXISTS");
        }
        medicineDTO.setCreatedAt(LocalDateTime.now());
        medicineDTO.setStock(0);
        return medicineRepo.save(medicineDTO.toEntity()).getId();
    }

    @Override
    public MedicineDTO getMedicineById(Long id) throws HmsException {
        return medicineRepo.findById(id).orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND")).toDTO();
    }

    @Override
    public void updateMedicine(MedicineDTO medicineDTO) throws HmsException {
        Medicine medicine = medicineRepo.findById(medicineDTO.getId()).orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"));
        Optional<Medicine> optional = medicineRepo.findByNameIgnoreCaseAndDosageIgnoreCase(medicineDTO.getName(), medicineDTO.getDosage());
        if(optional.isPresent() && !optional.get().getId().equals(medicineDTO.getId())){
            throw new HmsException("MEDICINE_ALREADY_EXISTS");
        }
        medicine.setName(medicineDTO.getName());
        medicine.setDosage(medicineDTO.getDosage());
        medicine.setMedicineType(medicineDTO.getMedicineType());
        medicine.setCategory(medicineDTO.getCategory());
        medicine.setManufacturer(medicineDTO.getManufacturer());
        medicine.setUnitPrice(medicineDTO.getUnitPrice());
        medicineRepo.save(medicine);
    }

    @Override
    public List<MedicineDTO> getAllMedicines() {
        return  ((List<Medicine>)medicineRepo.findAll()).stream().map(Medicine::toDTO).toList();
    }

    @Override
    public Integer getStockById(Long id) throws HmsException {
        return medicineRepo.findStockById(id).orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"));
    }

    @Override
    public Integer addStock(Long id, Integer quantity) throws HmsException {
        Medicine medicine = medicineRepo.findById(id).orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"));
        medicine.setStock(medicine.getStock()!=null? medicine.getStock()+quantity:quantity);
        medicineRepo.save(medicine);
        return medicine.getStock();
    }

    @Override
    public Integer removeStock(Long id, Integer quantity) throws HmsException {
        Medicine medicine = medicineRepo.findById(id).orElseThrow(()->new HmsException("MEDICINE_NOT_FOUND"));
        if(medicine.getStock()!=null){
            if(medicine.getStock()<quantity){
                medicine.setStock(0);
            }
            else{
                medicine.setStock(medicine.getStock()-quantity);
            }
        }
        else{
            medicine.setStock(0);
        }
        medicineRepo.save(medicine);
        return medicine.getStock();
    }
}
