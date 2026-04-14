package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.SaleItemDTO;
import com.hms.pharmacy.entity.SaleItem;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.SaleItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleItemServiceImpl implements SaleItemService{

    private final SaleItemRepo saleItemRepo;

    @Override
    public Long createSaleItem(SaleItemDTO saleItemDTO) throws HmsException {
        return saleItemRepo.save(saleItemDTO.toEntity()).getId();
    }

    @Override
    public void createSaleMultipleSaleItems(Long saleId, List<SaleItemDTO> saleItems) throws HmsException {
        saleItems.stream().map((x)->{
            x.setSaleId(saleId);
            return x.toEntity();
        }).forEach(saleItemRepo::save);
    }

    @Override
    public Long updateSaleItem(SaleItemDTO saleItemDTO) throws HmsException {
        SaleItem existing = saleItemRepo.findById(saleItemDTO.getId()).orElseThrow(()->new HmsException("SALE_ITEM_NOT_FOUND"));
        existing.setBatchNo(saleItemDTO.getBatchNo());
        existing.setQuantity(saleItemDTO.getQuantity());
        existing.setUnitPrice(saleItemDTO.getUnitPrice());
        return saleItemRepo.save(existing).getId();
    }

    @Override
    public List<SaleItemDTO> getSaleItemsBySaleId(Long saleId) throws HmsException {
        return saleItemRepo.findBySaleId(saleId).stream()
                .map(SaleItem::toDTO)
                .toList();
    }

    @Override
    public SaleItemDTO getSaleItem(Long id) throws HmsException {
        return saleItemRepo.findById(id).orElseThrow(()->new HmsException("SALE_ITEM_NOT_FOUND")).toDTO();
    }
}
