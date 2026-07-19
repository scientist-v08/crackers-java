package com.crackers.vinayakatraders.controller;

import com.crackers.vinayakatraders.dto.PriceListResponse;
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
    public ResponseEntity<List<PriceListResponse>> getAllPriceList() {
        List<PriceList> obtainedPriceList = this.priceListService.getAllPriceList();
        List<PriceListResponse> priceListResponse = obtainedPriceList.stream().map(opl -> new PriceListResponse(opl.getId(), opl.getItem(), opl.getPrice())).toList();
        return ResponseEntity.ok(priceListResponse);
    }

}
