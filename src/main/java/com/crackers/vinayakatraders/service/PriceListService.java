package com.crackers.vinayakatraders.service;

import com.crackers.vinayakatraders.entity.PriceList;
import com.crackers.vinayakatraders.repository.PriceListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PriceListService {

    private final PriceListRepository priceListRepository;

    public List<PriceList> getAllPriceList() {
        return this.priceListRepository.findAll();
    }

}
