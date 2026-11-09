package com.crackers.vinayakatraders.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

// Never remove RESERVED, reorder these constants, or insert new constants between them. Doing so changes the ordinal mappings.
public enum InventoryState {
    RESERVED,
    ORDERED,
    RECEIVED,
    UNPACKED;

    @JsonValue
    public String apiValue() {
        return switch (this) {
            case ORDERED -> "Ordered";
            case RECEIVED -> "Received";
            case UNPACKED -> "Unpacked";
            case RESERVED -> throw new IllegalStateException(
                    "RESERVED is not an API state");
        };
    }

    @JsonCreator
    public static InventoryState fromApiValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Invalid inventory state");
        }

        return switch (value.trim().toUpperCase(Locale.ROOT)) {
            case "ORDERED" -> ORDERED;
            case "RECEIVED" -> RECEIVED;
            case "UNPACKED" -> UNPACKED;
            default -> throw new IllegalArgumentException(
                    "Invalid inventory state: " + value);
        };
    }
}
