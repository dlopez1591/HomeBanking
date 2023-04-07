package com.mindhub.homebanking.utils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.TransactionDTO;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class PdfGenerator {

    public static void generatePdf(String filename, List<TransactionDTO> transactions) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            for(TransactionDTO transaction: transactions) {
                document.add(new Paragraph(transaction.toString()));
            }
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
