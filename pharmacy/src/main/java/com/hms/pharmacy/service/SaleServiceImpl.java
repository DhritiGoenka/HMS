package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.entity.Sale;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.SaleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService{
    private final SaleRepo saleRepo;

    @Override
    public Long createSale(SaleDTO saleDTO) throws HmsException {
        if(saleRepo.existsByPrescriptionId(saleDTO.getPrescriptionId())){
            throw new HmsException("SALE_ALREADY_EXISTS_WITH_PRESCRIPTION");
        }
        saleDTO.setSaleDate(LocalDateTime.now());
        return saleRepo.save(saleDTO.toEntity()).getId();
    }

    @Override
    public Long updateSale(SaleDTO saleDTO) throws HmsException {
        Sale sale = saleRepo.findById(saleDTO.getId()).orElseThrow(()->new HmsException("SALE_NOT_FOUND"));
        sale.setSaleDate(saleDTO.getSaleDate());
        sale.setTotalAmount(saleDTO.getTotalAmount());
        return saleRepo.save(sale).getId();
    }

    @Override
    public SaleDTO getSale(Long id) throws HmsException {
        return saleRepo.findById(id).orElseThrow(()->new HmsException("SALE_NOT_FOUND")).toDTO();
    }

    @Override
    public SaleDTO getSaleByPrescriptionId(Long prescriptionId) throws HmsException {
        return saleRepo.findByPrescriptionId(prescriptionId).orElseThrow(()->new HmsException("SALE_NOT_FOUND")).toDTO();
    }
}
