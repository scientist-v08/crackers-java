package com.crackers.vinayakatraders.dto;

import com.crackers.vinayakatraders.entity.Inventory;
import com.crackers.vinayakatraders.entity.InventoryState;

public record InventoryDto(
        Long id,
        String brandOrCompany,
        String state,
        String item,
        Integer numOfBoxes,
        Integer numOfCartons,
        Integer pricePerCarton,
        Integer subTotal
) {
    public static InventoryDto from(Inventory inventory) {
        String stateName = InventoryState.fromCode(inventory.getState()).apiValue();
        return new InventoryDto(
                inventory.getId(),
                inventory.getBrandOrCompany(),
                stateName,
                inventory.getItem(),
                inventory.getNumOfBoxes(),
                inventory.getNumOfCartons(),
                inventory.getPricePerCarton(),
                inventory.getSubTotal()
        );
    }
}
