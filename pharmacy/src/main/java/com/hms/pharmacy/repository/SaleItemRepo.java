package com.hms.pharmacy.repository;

import com.hms.pharmacy.entity.Sale;
import com.hms.pharmacy.entity.SaleItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SaleItemRepo extends CrudRepository<SaleItem,Long> {
    List<SaleItem> findBySaleId(Long saleId);
    List<SaleItem> findByMedicineId(Long medicineId);
}
