package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.exception.HmsException;

public interface SaleService {
    public Long createSale(SaleDTO saleDTO) throws HmsException;
    public Long updateSale(SaleDTO saleDTO) throws HmsException;
    public SaleDTO getSale(Long id) throws HmsException;
    public SaleDTO getSaleByPrescriptionId(Long prescriptionId) throws HmsException;
}
