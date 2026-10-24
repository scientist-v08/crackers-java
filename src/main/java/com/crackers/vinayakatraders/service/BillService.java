package com.crackers.vinayakatraders.service;

import com.crackers.vinayakatraders.dto.BillItemsRequest;
import com.crackers.vinayakatraders.dto.PreviewBillRequest;
import com.crackers.vinayakatraders.dto.PreviewBillResponse;
import com.crackers.vinayakatraders.dto.SubTotalGrandTotal;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

@Service
public class BillService {

    public PreviewBillResponse generateBill(PreviewBillRequest request) {
        SubTotalGrandTotal calculations = validateBill(request);
        byte[] pdfBytes = createPdf(request, calculations);

        String timestamp = LocalDateTime.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "bill_preview_" + timestamp + ".pdf";

        return new PreviewBillResponse(pdfBytes, filename);
    }

    private SubTotalGrandTotal validateBill(PreviewBillRequest req) {
        if (req.billItems() == null || req.billItems().isEmpty()) {
            throw new IllegalArgumentException("Bill items cannot be empty");
        }

        // Calculate grand total from items
        double discountMultiplier = this.parseDiscount(req.discount());
        double grandTotal = 0;
        ArrayList<Double> values = new ArrayList<>();
        for (BillItemsRequest item: req.billItems()) {
            double mrp = item.mrpOrNet();
            double quantity = item.quantity();
            double subtotal = this.calculateSubTotal(item.item(), mrp, quantity, discountMultiplier);
            values.add(subtotal);
            grandTotal += subtotal;
        }
        grandTotal = Math.round(grandTotal * 100.0) / 100.0;

        // Finalized Amount must be within -10% of Grand Total
        if (req.finalizedAmt() != null && req.finalizedAmt() > 0) {
            double minAllowed = Math.ceil(grandTotal * 0.9);

            if (req.finalizedAmt() < minAllowed) {
                throw new IllegalArgumentException(
                        String.format(Locale.US, "Finalized amount must be greater than %.0f (above -10%% of Grand Total)", minAllowed)
                );
            }
        }

        // Return grand total
        return new SubTotalGrandTotal(grandTotal, values);
    }

    private byte[] createPdf(PreviewBillRequest bill, SubTotalGrandTotal calc) {
        float MM_TO_PT = 72f / 25.4f;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 10 * MM_TO_PT, 10 * MM_TO_PT, 10 * MM_TO_PT, 10 * MM_TO_PT);
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Fonts
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            // Title
            Paragraph title = new Paragraph("Vinayaka Traders", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(5 * MM_TO_PT);
            document.add(title);

            // Customer & Terms Section
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidths(new float[]{90, 90});
            headerTable.setTotalWidth(190 * MM_TO_PT);
            headerTable.setLockedWidth(true);
            headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            // Left - Customer Info
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setPadding(0);
            leftCell.addElement(new Paragraph("Customer: " + bill.user(), normalFont));
            leftCell.addElement(new Paragraph("Mobile: " + bill.mobile(), normalFont));
            leftCell.addElement(new Paragraph("Bill ID: " + "NA", normalFont)); // or pass billId
            headerTable.addCell(leftCell);

            // Right - Terms
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setPadding(0);
            Paragraph terms = new Paragraph("T&C: Quality not guaranteed by retailer. Contact brand for complaints.", normalFont);
            terms.setAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(terms);
            headerTable.addCell(rightCell);

            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(7);
            table.setWidths(new float[]{15, 58, 23, 18, 28, 23, 25});
            table.setTotalWidth(190 * MM_TO_PT);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setSplitRows(false);

            // Headers
            String[] headers = {"Sl.No", "Item", "MRP/Net", "Quantity", "SubTotal w/o Disc", "Discount", "SubTotal"};
            Color headerColor = new Color(217, 221, 220);
            float headerHeight = 12 * MM_TO_PT;
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, boldFont));
                cell.setBackgroundColor(headerColor);
                cell.setBorder(Rectangle.BOX);
                cell.setBorderWidth(0.5f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(0);
                cell.setFixedHeight(headerHeight);
                table.addCell(cell);
            }

            // Rows
            int i =0;
            for (BillItemsRequest item : bill.billItems()) {
                // SlNo
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.slNo()) + ".", normalFont)));

                // Item
                table.addCell(new PdfPCell(new Phrase(item.item(), normalFont)));

                // MRP/Net
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.mrpOrNet()), normalFont)));

                // Quantity
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.quantity()), normalFont)));

                // SubTotal without discount = MRP * Qty
                double subNoDisc = Math.round(item.mrpOrNet() * item.quantity() * 100.0) / 100.0;
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", subNoDisc), normalFont)));

                // Discount
                table.addCell(new PdfPCell(new Phrase(item.discount() != null ? item.discount() : "", normalFont)));


                // SubTotal
                table.addCell(new PdfPCell(new Phrase(String.valueOf(calc.subtotals().get(i)), normalFont)));
                i++;
            }

            document.add(table);

            // Grand Total
            document.add(new Paragraph("\n"));
            Paragraph grandTotalPara = new Paragraph("Grand Total: " + calc.grandTotal(), totalFont);
            grandTotalPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(grandTotalPara);

            // Finalized Amount
            if (bill.finalizedAmt() != null && bill.finalizedAmt() > 0) {
                Paragraph finalized = new Paragraph("Finalized Amount: " + bill.finalizedAmt(), totalFont);
                finalized.setAlignment(Element.ALIGN_RIGHT);
                document.add(finalized);
            }

            document.close();
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private double parseDiscount(String discount) {

        String value = discount.trim();

        if (value.endsWith("%")) {
            value = value.substring(0, value.length() - 1).trim();
        }

        double parsed;

        try {
            parsed = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid discount: " + discount, ex);
        }

        if (!Double.isFinite(parsed)) {
            throw new IllegalArgumentException("Invalid discount: " + discount);
        }

        return parsed >= 1
                ? 1 - (parsed / 100.0)
                : parsed;
    }

    private double calculateSubTotal(
            String item,
            double mrpOrNet,
            double quantity,
            double discountMultiplier
    ) {

        double subtotal = item.startsWith("Other")
                ? mrpOrNet * quantity
                : mrpOrNet * quantity * discountMultiplier;

        return Math.round(subtotal * 100.0) / 100.0;
    }

}
