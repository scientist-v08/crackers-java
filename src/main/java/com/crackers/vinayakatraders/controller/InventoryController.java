package com.crackers.vinayakatraders.controller;

import com.crackers.vinayakatraders.dto.InventoryListResponse;
import com.crackers.vinayakatraders.dto.InventoryRequest;
import com.crackers.vinayakatraders.dto.UpdateInventoryRequest;
import com.crackers.vinayakatraders.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@Validated
public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/inventory")
    public ResponseEntity<InventoryListResponse> getInventory(
            @RequestParam(defaultValue = "1") @Min(1) int pageNumber,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int pageSize,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(required = false) String state,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ) {
        return ResponseEntity.ok(service.getInventory(pageNumber, pageSize, title, state));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/inventory")
    public ResponseEntity<Map<String, String>> addInventory(@Valid @RequestBody InventoryRequest request) {
        service.addItem(request);
        return ResponseEntity.ok(
                Map.of("Success", "Item saved to inventory")
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/updateInventory")
    public ResponseEntity<Map<String, String>> updateInventory(@Valid @RequestBody UpdateInventoryRequest request) {
        service.updateInventory(request);
        return ResponseEntity.ok(
                Map.of("message", "Inventory updated successfully")
        );
    }
}
