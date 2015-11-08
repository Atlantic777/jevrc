package net.devbase.jevrc.viewer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import net.devbase.jevrc.viewer.models.EvrcDocumentInfoModel;
import net.devbase.jevrc.viewer.models.EvrcOwnerInfoModel;
import net.devbase.jevrc.viewer.models.EvrcVehicleInfoModel;

public class PdfReport
{
  private EvrcDocumentInfoModel documentInfo;
  private EvrcVehicleInfoModel vehicleInfo;
  private EvrcOwnerInfoModel ownerInfo;
  
  public PdfReport(EvrcDocumentInfoModel documentInfo, EvrcVehicleInfoModel vehicleInfo, EvrcOwnerInfoModel ownerInfo)
  {
    this.documentInfo = documentInfo;
    this.vehicleInfo = vehicleInfo;
    this.ownerInfo = ownerInfo;
  }
  
  public void write(String filename)
    throws IOException, DocumentException
  {
    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream(filename));
    document.open();
    
    PdfPTable documentTable = new PdfPTable(2);
    for (int row = 0; row < this.documentInfo.getRowCount(); row++) {
      for (int col = 0; col < 2; col++) {
        documentTable.addCell((String)this.documentInfo.getValueAt(row, col));
      }
    }
    PdfPTable vehicleTable = new PdfPTable(2);
    for (int row = 0; row < this.vehicleInfo.getRowCount(); row++) {
      for (int col = 0; col < 2; col++) {
        vehicleTable.addCell((String)this.vehicleInfo.getValueAt(row, col));
      }
    }
    PdfPTable ownerTable = new PdfPTable(2);
    for (int row = 0; row < this.ownerInfo.getRowCount(); row++) {
      for (int col = 0; col < 2; col++) {
        ownerTable.addCell((String)this.ownerInfo.getValueAt(row, col));
      }
    }
    Paragraph p1 = new Paragraph();
    Paragraph p2 = new Paragraph();
    Paragraph p3 = new Paragraph();
    
    p1.add(new Phrase("Podaci o dokumentu"));
    p1.add(documentTable);
    
    p2.add(new Phrase("Podaci o vozilu"));
    p2.add(vehicleTable);
    
    p3.add(new Phrase("Podaci o vlasniku"));
    p3.add(ownerTable);
    
    document.add(p1);
    document.add(p2);
    document.add(p3);
    
    document.close();
  }
}
