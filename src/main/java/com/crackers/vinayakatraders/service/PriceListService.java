package com.crackers.vinayakatraders.service;

import com.crackers.vinayakatraders.dto.PriceListResponse;
import com.crackers.vinayakatraders.dto.ProductsList;
import com.crackers.vinayakatraders.entity.PriceList;
import com.crackers.vinayakatraders.repository.PriceListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PriceListService {

    private final PriceListRepository priceListRepository;

    public List<ProductsList> getAllPriceList() {
        // Get all price lists from DB
        List<PriceList> obtainedPriceList = this.priceListRepository.findAll();

        // Group the price list by brand using map
        Map<String, List<PriceListResponse>> grouped = new LinkedHashMap<>();
        for (PriceList item: obtainedPriceList) {
            grouped.computeIfAbsent(
                    item.getBrand(),
                    brand -> new ArrayList<>()
            ).add(
                    new PriceListResponse(item.getId(), item.getItem(), item.getPrice(), item.getBrand())
            );
        }

        // Now create the required price list response
        List<ProductsList> result = new ArrayList<>();
        for (Map.Entry<String, List<PriceListResponse>> entry: grouped.entrySet()) {
            result.add(new ProductsList(entry.getKey(), entry.getValue()));
        }

        // Sort by brand and return
        result.sort(Comparator.comparing(ProductsList::brand));
        return result;
    }

}
