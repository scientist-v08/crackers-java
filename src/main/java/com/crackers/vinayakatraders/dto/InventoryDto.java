package com.crackers.vinayakatraders.dto;

import com.crackers.vinayakatraders.entity.Inventory;

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
        return new InventoryDto(
                inventory.getId(),
                inventory.getBrandOrCompany(),
                inventory.getState().apiValue(),
                inventory.getItem(),
                inventory.getNumOfBoxes(),
                inventory.getNumOfCartons(),
                inventory.getPricePerCarton(),
                inventory.getSubTotal()
        );
    }
}
