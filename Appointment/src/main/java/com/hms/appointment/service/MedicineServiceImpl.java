package com.hms.appointment.service;

import com.hms.appointment.dto.MedicineDTO;
import com.hms.appointment.entity.Medicine;
import com.hms.appointment.repository.MedicineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService{

    private final MedicineRepo medicineRepo;

    @Override
    public Long saveMedicine(MedicineDTO medicineDTO) {
        return medicineRepo.save(medicineDTO.toEntity()).getId();
    }

    @Override
    public List<MedicineDTO> saveAllMedicines(List<MedicineDTO> medicineList){
        return ((List<Medicine>)medicineRepo.saveAll(
                medicineList.stream().map(MedicineDTO::toEntity).toList()
        )).stream().map(Medicine::toDTO).toList();

    }

    @Override
    public List<MedicineDTO> getAllMedicinesByPrescriptionId(Long prescriptionId) {
        return ((List<Medicine>) medicineRepo.findAllByPrescription_Id(prescriptionId))
                .stream().map(Medicine::toDTO).toList();
    }
}
