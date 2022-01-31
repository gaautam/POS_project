package com.increff.pos.service;

import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.ReportForm;

@Service
public class ReportService {
	
	//brand report
	public void brandReport(List<BrandData> list) {
		
		 new ReportService().createTableBrand("E:\\Increff Tutorials\\pos-spring-master\\employee-spring-master\\src\\main\\webapp\\resources\\Brand_Report.pdf",list);
	}
	
	private void createTableBrand(String PDFPath, List<BrandData> list){
	    try {
	      File f= new File(PDFPath); 
	      f.delete();
	      Font font = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);
	      Document doc = new Document();
	      PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(PDFPath));
	      PdfPTable table = new PdfPTable(2);
	      table.setWidthPercentage(100);
	      // setting column widths
	      table.setWidths(new float[] {12.0f, 12.0f});
	      PdfPCell cell = new PdfPCell();
	      // table headers
	      cell.setPhrase(new Phrase("Brand", font));
	      table.addCell(cell);
	      cell.setPhrase(new Phrase("Category", font));
	      table.addCell(cell);
	      // adding table rows
	      for(BrandData bd : list) {
	        table.addCell(bd.getBrand());
	        table.addCell(bd.getCategory());  
	      }
	      doc.open();
	      // adding table to document
	      doc.add(table);
	      doc.close();
	      writer.close();
	      System.out.println("Brand report created successfully");
	    } catch (DocumentException | FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }
	
	//inventory report
	public void inventoryReport(List<InventoryData> list) {
		
		 new ReportService().createTableInventory("E:\\Increff Tutorials\\pos-spring-master\\employee-spring-master\\src\\main\\webapp\\resources\\Inventory_Report.pdf",list);
	}
	
	private void createTableInventory(String PDFPath, List<InventoryData> list){
	    try {
	      File f= new File(PDFPath); 
		  f.delete();
	      Font font = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);
	      Document doc = new Document();
	      PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(PDFPath));
	      PdfPTable table = new PdfPTable(2);
	      table.setWidthPercentage(100);
	      // setting column widths
	      table.setWidths(new float[] {12.0f, 12.0f});
	      PdfPCell cell = new PdfPCell();
	      // table headers
	      cell.setPhrase(new Phrase("Barcode", font));
	      table.addCell(cell);
	      cell.setPhrase(new Phrase("Quantity", font));
	      table.addCell(cell);
	      // adding table rows
	      for(InventoryData bd : list) {
	        table.addCell(bd.getBarcode());
	        table.addCell(String.valueOf(bd.getQuantity()));  
	      }
	      doc.open();
	      // adding table to document
	      doc.add(table);
	      doc.close();
	      writer.close();
	      System.out.println("Inventory report created successfully");
	    } catch (DocumentException | FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }
	
	public void OrderReport(List<ReportForm> list,String brand,String category) {
		
		 new ReportService().createTableOrder("E:\\Increff Tutorials\\pos-spring-master\\employee-spring-master\\src\\main\\webapp\\resources\\Order_Report.pdf",list,brand,category);
	}
	
	private void createTableOrder(String PDFPath, List<ReportForm> list,String brand,String category){
	    try {
	      File f= new File(PDFPath); 
		  f.delete();
	      Font font = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);
	      Document doc = new Document();
	      PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(PDFPath));
	      PdfPTable table = new PdfPTable(4);
	      table.setWidthPercentage(100);
	      // setting column widths
	      table.setWidths(new float[] {6.0f, 6.0f, 6.0f, 6.0f});
	      PdfPCell cell = new PdfPCell();
	      // table headers
	      cell.setPhrase(new Phrase("Brand", font));
	      table.addCell(cell);
	      cell.setPhrase(new Phrase("Category", font));
	      table.addCell(cell);
	      cell.setPhrase(new Phrase("Quantity", font));
	      table.addCell(cell);
	      cell.setPhrase(new Phrase("Total Revenue", font));
	      table.addCell(cell);
	      // adding table rows
	      brand = brand.replace("undefined-undefined-", "");
	      category = category.replace("undefined-undefined-", "");
	      int final_quantity = 0;
	      double total_revenue = 0;
	      for(ReportForm bd : list) {
	    	if(bd.getBrand().equals(brand) && bd.getCategory().equals(category)) {
	         final_quantity = final_quantity+bd.getQuantity();
	         total_revenue = total_revenue+bd.getSelling_price();
	    	}
	      }
	      table.addCell(brand);
	      table.addCell(category);
	      table.addCell(String.valueOf(final_quantity));  
	      table.addCell(String.valueOf(total_revenue)); 
	      doc.open();
	      // adding table to document
	      doc.add(table);
	      doc.close();
	      writer.close();
	      System.out.println("Order report created successfully");
	    } catch (DocumentException | FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }
	
	public void invoiceGenerator(List<OrderItemData> list,OrderData order) {
			
		 double total_price = 0;
		 for(OrderItemData l:list) {
			 total_price= total_price+l.getSelling_price();
		 }
		 
		 new ReportService().createTableOrderItems("E:\\Increff Tutorials\\pos-spring-master\\employee-spring-master\\src\\main\\webapp\\resources\\Order_Invoice.pdf",list,order,total_price);
	}
	
	private void createTableOrderItems(String PDFPath, List<OrderItemData> list,OrderData order,double total_price){
	    try {
	      File f= new File(PDFPath); 
		  f.delete();
	      Font font = new Font(Font.HELVETICA, 15, Font.BOLDITALIC);
	      Document doc = new Document();
	      PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(PDFPath));
	      String date = order.getCreateDate();
	      String order_id = String.valueOf(order.getId());
	      String final_para ="Order ID: " + order_id +"  Created Date: " + date;
	      String final_total = " Total Selling Price : " + String.valueOf(total_price);
	      Paragraph para = new Paragraph(final_para,font);
	      para.setAlignment(Paragraph.ALIGN_CENTER);
	      Paragraph total = new Paragraph(final_total,font);
	      total.setAlignment(Paragraph.ALIGN_RIGHT);
	      Paragraph p = new Paragraph(" ",font);
	      p.setAlignment(Paragraph.ALIGN_CENTER);
	      Paragraph title = new Paragraph("INVOICE PDF",font);
	      
	      PdfPTable table = new PdfPTable(3);
	      table.setWidthPercentage(100);
	      // setting column widths
	      table.setWidths(new float[] {8.0f,8.0f,8.0f});
	      PdfPCell cell = new PdfPCell();
	      // table headers
	      cell.setPhrase(new Phrase("Barcode", font));
	      table.addCell(cell);
	      cell.setPhrase(new Phrase("Quantity", font));
	      table.addCell(cell);
	      cell.setPhrase(new Phrase("Selling Price", font));
	      table.addCell(cell);
	      // adding table rows
	      for(OrderItemData bd : list) {
	        table.addCell((bd.getBarcode()));
	        table.addCell(String.valueOf(bd.getQuantity())); 
	        table.addCell(String.valueOf(bd.getSelling_price()));
	      }
	      doc.open();
	      //adding Order and date details to document
	      doc.add(title);
	      doc.add(p);
	      doc.add(para);
	      doc.add(p);
	      // adding table to document
	      doc.add(table);
	      doc.add(p);
	      doc.add(total);
	      doc.close();
	      writer.close();
	      System.out.println("Order items report created successfully");
	    } catch (DocumentException | FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }
}
