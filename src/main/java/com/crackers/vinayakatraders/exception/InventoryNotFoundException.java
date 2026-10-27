package com.crackers.vinayakatraders.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException() {
        super("inventory record not found");
    }
}
