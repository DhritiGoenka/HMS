package com.hms.pharmacy.api;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.dto.SaleItemDTO;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.service.SaleItemService;
import com.hms.pharmacy.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/pharmacy/sales")
@RequiredArgsConstructor
public class SaleAPI {
    private final SaleService saleService;
    private final SaleItemService saleItemService;

    @PostMapping("/create")
    public ResponseEntity<Long> createSaleItem(@RequestBody SaleDTO saleDTO) throws HmsException{
        return new ResponseEntity<>(saleService.createSale(saleDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Long> updateSaleItem(@RequestBody SaleDTO saleDTO) throws HmsException{
        return new ResponseEntity<>(saleService.updateSale(saleDTO), HttpStatus.OK);
    }

    @GetMapping("/getSale/{id}")
    public ResponseEntity<SaleDTO> getSale(@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(saleService.getSale(id),HttpStatus.OK);
    }

    @GetMapping("/getSaleItems/{saleId}")
    public ResponseEntity<List<SaleItemDTO>> getSaleItems(@PathVariable Long saleId) throws HmsException{
        return new ResponseEntity<>(saleItemService.getSaleItemsBySaleId(saleId),HttpStatus.OK);
    }
}
