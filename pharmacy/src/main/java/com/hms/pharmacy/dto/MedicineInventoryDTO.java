package com.hms.pharmacy.dto;

import com.hms.pharmacy.entity.Medicine;
import com.hms.pharmacy.entity.MedicineInventory;
import com.hms.pharmacy.entity.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineInventoryDTO {
    private Long id;
    private Long medicineId;
    private String batchNo;
    private Integer quantity; //current quantity of batch
    private LocalDate expiryDate;
    private LocalDate addedDate;
    private Integer initialQuantity; //initial quantity of batch
    private StockStatus status;

    public MedicineInventory toEntity(){
        return new MedicineInventory(
                id, new Medicine(medicineId),
                batchNo,
                quantity,
                expiryDate,
                addedDate,
                initialQuantity,
                status
        );
    }
}
