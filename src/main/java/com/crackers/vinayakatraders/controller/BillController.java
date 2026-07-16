package com.crackers.vinayakatraders.controller;

import com.crackers.vinayakatraders.dto.PreviewBillRequest;
import com.crackers.vinayakatraders.dto.PreviewBillResponse;
import com.crackers.vinayakatraders.service.BillService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BillController {

    private final BillService billService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/all/billing/preview")
    public ResponseEntity<byte[]> billPreview(@Valid @RequestBody PreviewBillRequest request) {
        PreviewBillResponse response = billService.generateBill(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(response.filename())
                        .build()
        );
        headers.setContentLength(response.file().length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(response.file());
    }

}
