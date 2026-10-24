package com.crackers.vinayakatraders.controller;

import com.crackers.vinayakatraders.dto.PriceListResponse;
import com.crackers.vinayakatraders.dto.ProductsList;
import com.crackers.vinayakatraders.entity.PriceList;
import com.crackers.vinayakatraders.service.PriceListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/all/price-list")
@AllArgsConstructor
public class PriceListController {

    private final PriceListService priceListService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<ProductsList>> getAllPriceList() {
        List<ProductsList> obtainedPriceList = this.priceListService.getAllPriceList();
        return ResponseEntity.ok(obtainedPriceList);
    }

}
