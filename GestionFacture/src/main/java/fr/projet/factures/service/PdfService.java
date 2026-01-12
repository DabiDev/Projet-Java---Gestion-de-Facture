package fr.projet.factures.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import fr.projet.factures.model.Facture;
import fr.projet.factures.model.LigneFacture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class PdfService {

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

    public void generateInvoicePdf(Facture facture, File file) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // 1. Header (Company Info)
        document.add(new Paragraph("GESTION FACTURES S.A.R.L", TITLE_FONT));
        document.add(new Paragraph("123 Avenue du Code", NORMAL_FONT));
        document.add(new Paragraph("75000 Paris, France", NORMAL_FONT));
        document.add(Chunk.NEWLINE);

        // 2. Invoice Info & Client Info
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{1, 1});

        // Left: Invoice Details
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.setBorder(Rectangle.NO_BORDER);
        invoiceCell.addElement(new Paragraph("FACTURE N°: " + facture.getNumero(), HEADER_FONT));
        invoiceCell.addElement(new Paragraph("Date: " + facture.getDate(), NORMAL_FONT));
        infoTable.addCell(invoiceCell);

        // Right: Client Details
        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        clientCell.addElement(new Paragraph("CLIENT:", HEADER_FONT));
        clientCell.addElement(new Paragraph(facture.getClient().getNom(), NORMAL_FONT));
        clientCell.addElement(new Paragraph(facture.getClient().getEmail(), NORMAL_FONT));
        infoTable.addCell(clientCell);

        document.add(infoTable);
        document.add(Chunk.NEWLINE);

        // 3. Products Table
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 5, 2, 2, 2});
        table.setHeaderRows(1);

        // Headers
        addHeaderCell(table, "Référence");
        addHeaderCell(table, "Description");
        addHeaderCell(table, "Quantité");
        addHeaderCell(table, "Prix Unit.");
        addHeaderCell(table, "Total");

        // Rows
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        
        for (LigneFacture ligne : facture.getLignes()) {
            addCell(table, "", Element.ALIGN_LEFT); // No Reference stored
            addCell(table, ligne.getDescription(), Element.ALIGN_LEFT);
            addCell(table, String.valueOf(ligne.getQuantite()), Element.ALIGN_CENTER);
            addCell(table, currency.format(ligne.getPrixUnitaire()), Element.ALIGN_RIGHT);
            addCell(table, currency.format(ligne.getMontantLigne()), Element.ALIGN_RIGHT);
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        // 4. Total
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40); // Small table on right
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        PdfPCell labelCell = new PdfPCell(new Phrase("TOTAL GENERAL", HEADER_FONT));
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(currency.format(facture.getTotal()), HEADER_FONT));
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.addCell(valueCell);

        document.add(totalTable);

        // Footer
        document.add(Chunk.NEWLINE);
        Paragraph footer = new Paragraph("Merci de votre confiance.", NORMAL_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setHorizontalAlignment(align);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
