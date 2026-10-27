package com.crackers.vinayakatraders.entity;

import java.util.Locale;

/** API-facing names mapped to the SMALLINT values persisted in PostgreSQL. */
public enum InventoryState {
    ORDERED((short) 1, "Ordered"),
    RECEIVED((short) 2, "Received"),
    UNPACKED((short) 3, "Unpacked");

    private final short code;
    private final String apiValue;

    InventoryState(short code, String apiValue) {
        this.code = code;
        this.apiValue = apiValue;
    }

    public short code() {
        return code;
    }

    public String apiValue() {
        return apiValue;
    }

    public static InventoryState fromCode(short code) {
        for (InventoryState state : values()) {
            if (state.code == code) return state;
        }
        throw new IllegalArgumentException("Invalid inventory state code: " + code);
    }

    /** Accepts frontend values such as "Ordered", "Received", or "Unpacked". */
    public static InventoryState fromApiValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("state must be Ordered, Received, or Unpacked");
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "ORDERED" -> ORDERED;
            case "RECEIVED" -> RECEIVED;
            case "UNPACKED" -> UNPACKED;
            default -> throw new IllegalArgumentException(
                    "Invalid inventory state: " + value + ". Expected Ordered, Received, or Unpacked.");
        };
    }
}
