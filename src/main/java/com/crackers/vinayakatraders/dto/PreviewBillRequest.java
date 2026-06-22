package com.crackers.vinayakatraders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record PreviewBillRequest(
        @NotBlank String user,
        @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits") String mobile,
        Integer grandTotal,
        @NotEmpty List<BillItemsRequest> billItems,
        Integer finalizedAmt
) {
}
