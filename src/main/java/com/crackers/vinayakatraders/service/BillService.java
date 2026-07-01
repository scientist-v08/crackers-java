package com.crackers.vinayakatraders.service;

import com.crackers.vinayakatraders.dto.BillItemsRequest;
import com.crackers.vinayakatraders.dto.PreviewBillRequest;
import com.crackers.vinayakatraders.dto.PreviewBillResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class BillService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS").withZone(ZoneId.systemDefault());

    public PreviewBillResponse generateBill(PreviewBillRequest request) {
        validateBill(request);
        byte[] pdfBytes = createPdf(request);

        String timestamp = FORMATTER.format(Instant.now());
        String filename = "previewBill_" + timestamp + ".pdf";

        return new PreviewBillResponse(pdfBytes, filename);
    }

    private void validateBill(PreviewBillRequest req) {
        if (req.billItems() == null || req.billItems().isEmpty()) {
            throw new IllegalArgumentException("Bill items cannot be empty");
        }

        // Calculate grand total from items
        int calculatedGrandTotal = req.billItems().stream()
                .mapToInt(item -> item.subTotal() != null ? item.subTotal() : 0)
                .sum();

        if (req.grandTotal() == null || req.grandTotal() != calculatedGrandTotal) {
            throw new IllegalArgumentException(
                    String.format("Grand Total mismatch. Received: %d, Calculated: %d", req.grandTotal(), calculatedGrandTotal)
            );
        }

        // Finalized Amount must be within ±10% of Grand Total
        if (req.finalizedAmt() != null && req.finalizedAmt() > 0) {
            int minAllowed = (int) Math.ceil(calculatedGrandTotal * 0.9);
            int maxAllowed = (int) Math.floor(calculatedGrandTotal * 1.1);

            if (req.finalizedAmt() < minAllowed || req.finalizedAmt() > maxAllowed) {
                throw new IllegalArgumentException(
                        String.format("Finalized amount must be between %d and %d (within ±10%% of Grand Total)", minAllowed, maxAllowed)
                );
            }
        }
    }

    private byte[] createPdf(PreviewBillRequest bill) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fonts
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11);
            Font smallBold = new Font(Font.HELVETICA, 12, Font.BOLD);

            // Title
            Paragraph title = new Paragraph("Vinayaka Traders", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Customer & Terms Section
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{50, 50});

            // Left - Customer Info
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.addElement(new Paragraph("Customer: " + bill.user(), normalFont));
            leftCell.addElement(new Paragraph("Mobile: " + "NA"));
            leftCell.addElement(new Paragraph("Bill ID: " + "NA")); // or pass billId
            headerTable.addCell(leftCell);

            // Right - Terms
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.addElement(new Paragraph("T&C: Quality not guaranteed by retailer. Contact brand for complaints.", normalFont));
            headerTable.addCell(rightCell);

            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{8, 32, 12, 10, 12, 14, 12});

            // Headers
            String[] headers = {"SlNo", "Item", "MRP/Net", "Quantity", "Discount", "SubTotal w/o Disc", "SubTotal"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, boldFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Rows
            for (BillItemsRequest item : bill.billItems()) {
                // SlNo
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.slNo()), normalFont)));

                // Item
                table.addCell(new PdfPCell(new Phrase(item.item(), normalFont)));

                // MRP/Net
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.mrpOrNet()), normalFont)));

                // Quantity
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.quantity()), normalFont)));

                // Discount
                table.addCell(new PdfPCell(new Phrase(item.discount() != null ? item.discount() : "", normalFont)));

                // SubTotal without discount = MRP * Qty
                double subNoDisc = (item.mrpOrNet() != null ? item.mrpOrNet() : 0) *
                        (item.quantity() != null ? item.quantity() : 0);
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", subNoDisc), normalFont)));

                // SubTotal
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.subTotal()), normalFont)));
            }

            document.add(table);

            // Grand Total
            document.add(new Paragraph("\n"));
            Paragraph grandTotal = new Paragraph("Grand Total: " + bill.grandTotal(), smallBold);
            grandTotal.setAlignment(Element.ALIGN_RIGHT);
            document.add(grandTotal);

            // Finalized Amount
            if (bill.finalizedAmt() != null && bill.finalizedAmt() > 0) {
                Paragraph finalized = new Paragraph("Finalized Amount: " + bill.finalizedAmt(),
                        new Font(Font.HELVETICA, 13, Font.BOLD));
                finalized.setAlignment(Element.ALIGN_RIGHT);
                document.add(finalized);
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

}
