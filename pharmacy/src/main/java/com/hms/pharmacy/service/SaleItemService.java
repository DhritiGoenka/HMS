package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.SaleItemDTO;
import com.hms.pharmacy.exception.HmsException;

import java.util.List;

public interface SaleItemService {
    public Long createSaleItem(SaleItemDTO saleItemDTO) throws HmsException;
    public void createSaleMultipleSaleItems(Long saleId, List<SaleItemDTO> saleItems) throws HmsException;
    public Long updateSaleItem(SaleItemDTO saleItemDTO) throws HmsException;
    public List<SaleItemDTO> getSaleItemsBySaleId(Long saleId) throws HmsException;
    public SaleItemDTO getSaleItem(Long id) throws HmsException;
}
