package com.crackers.vinayakatraders.dto;

import java.util.List;

public record SubTotalGrandTotal(
        double grandTotal,
        List<Double> subtotals
) {
}
