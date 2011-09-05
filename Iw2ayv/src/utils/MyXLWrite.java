//package utils;
//
//import ij.IJ;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Locale;
//
//import jxl.CellView;
//import jxl.Workbook;
//import jxl.WorkbookSettings;
//import jxl.format.UnderlineStyle;
//import jxl.read.biff.BiffException;
//import jxl.write.Formula;
//import jxl.write.Label;
//import jxl.write.Number;
//import jxl.write.WritableCellFormat;
//import jxl.write.WritableFont;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import jxl.write.WriteException;
//import jxl.write.biff.RowsExceededException;
//
//public class MyXLWrite {
//
//	private WritableCellFormat timesBoldUnderline;
//	private WritableCellFormat times;
//	private String inputFile;
//
//	public void setOutputFile(String inputFile) {
//		this.inputFile = inputFile;
//	}
//
//	public void write() throws IOException, WriteException {
//
//		File file = null;
//		if (InputOutput.checkFile(inputFile)) { // if the file exist...
//			try {
//				Workbook w1 = Workbook.getWorkbook(new File(inputFile));
//			} catch (BiffException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			file = new File(inputFile);
//		}
//		
//		
//		WorkbookSettings wbSettings = new WorkbookSettings();
//
//		wbSettings.setLocale(new Locale("en", "EN"));
//
//		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
//		workbook.createSheet("Report0", 0);
//		WritableSheet excelSheet0 = workbook.getSheet(0);
//		createLabel(excelSheet0);
//		createContent(excelSheet0);
//		workbook.createSheet("Report1", 1);
//		WritableSheet excelSheet1 = workbook.getSheet(1);
//		createLabel(excelSheet1);
//		createContent(excelSheet1);
//		workbook.createSheet("Report2", 2);
//		WritableSheet excelSheet2 = workbook.getSheet(2);
//		createLabel(excelSheet2);
//		createContent(excelSheet2);
//		workbook.createSheet("Report3", 3);
//		WritableSheet excelSheet3 = workbook.getSheet(3);
//		createLabel(excelSheet3);
//		createContent(excelSheet3);
//
//		workbook.write();
//		workbook.close();
//	}
//
//	private void createLabel(WritableSheet sheet) throws WriteException {
//		// Lets create a times font
//		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
//		// Define the cell format
//		times = new WritableCellFormat(times10pt);
//		// Lets automatically wrap the cells
//		times.setWrap(true);
//
//		// Create create a bold font with unterlines
//		WritableFont times10ptBoldUnderline = new WritableFont(
//				WritableFont.TIMES, 10, WritableFont.BOLD, false,
//				UnderlineStyle.SINGLE);
//		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
//		// Lets automatically wrap the cells
//		timesBoldUnderline.setWrap(true);
//
//		CellView cv = new CellView();
//		cv.setFormat(times);
//		cv.setFormat(timesBoldUnderline);
//		cv.setAutosize(true);
//
//		// Write a few headers
//		addCaption(sheet, 0, 0, "Header 1");
//		addCaption(sheet, 1, 0, "This is another header");
//
//	}
//
//	private void createContent(WritableSheet sheet) throws WriteException,
//			RowsExceededException {
//		// Write a few number
//		for (int i = 1; i < 10; i++) {
//			// First column
//			addNumber(sheet, 0, i, i + 10);
//			// Second column
//			addNumber(sheet, 1, i, i * i);
//		}
//		// Lets calculate the sum of it
//		StringBuffer buf = new StringBuffer();
//		buf.append("SUM(A2:A10)");
//		Formula f = new Formula(0, 10, buf.toString());
//		sheet.addCell(f);
//		buf = new StringBuffer();
//		buf.append("SUM(B2:B10)");
//		f = new Formula(1, 10, buf.toString());
//		sheet.addCell(f);
//
//		// Now a bit of text
//		for (int i = 12; i < 20; i++) {
//			// First column
//			addLabel(sheet, 0, i, "Boring text " + i);
//			// Second column
//			addLabel(sheet, 1, i, "Another text");
//		}
//	}
//
//	private void addCaption(WritableSheet sheet, int column, int row, String s)
//			throws RowsExceededException, WriteException {
//		Label label;
//		label = new Label(column, row, s, timesBoldUnderline);
//		sheet.addCell(label);
//	}
//
//	private void addNumber(WritableSheet sheet, int column, int row,
//			Integer integer) throws WriteException, RowsExceededException {
//		Number number;
//		number = new Number(column, row, integer, times);
//		sheet.addCell(number);
//	}
//
//	private void addLabel(WritableSheet sheet, int column, int row, String s)
//			throws WriteException, RowsExceededException {
//		Label label;
//		label = new Label(column, row, s, times);
//		sheet.addCell(label);
//	}
//
//	// private boolean initializeHSSF(){
//	// if (checkFile(filePath)){ // if the file exist...
//	// try{ // we will try to load it...
//	// POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
//	// // load file
//	// wb = new HSSFWorkbook(fs); // load the workbook
//	// }catch(IOException ioE){
//	// System.out.println(ioE.getMessage());
//	// System.out.println("File Not Found, Or No ExcelFile");
//	// }
//	// int sheets = wb.getNumberOfSheets();
//	// if (debug)
//	// System.out.println("sheets: "+sheets);
//	// if (workSheetChoice()) {
//	// s = wb.createSheet("IJResults"+(sheets+1)); // now make a new worksheet
//	// }
//	// else{
//	// String[] wsList = wsList(wb);
//	// GenericDialog dg = new GenericDialog("Choose WorkSheet");
//	// dg.addChoice("Work Sheets", wsList, wsList[0]);
//	// dg.showDialog();
//	// if (dg.wasCanceled()) return false;
//	// int wsID = dg.getNextChoiceIndex();
//	//
//	// try{
//	// s = wb.getSheetAt(wsID);
//	// }catch(Exception e){
//	// System.out.println(e.getMessage());
//	// }
//	// }
//	// }
//	// else{ // if the file does not exist...
//	// wb = new HSSFWorkbook(); // we create a new file
//	// if (debug)
//	// System.out.println("new HSSFWorkbook()");
//	// int sheets = wb.getNumberOfSheets();
//	// s = wb.createSheet("IJResults"+(sheets+1)); // now make a new worksheet
//	// if (debug)
//	// System.out.println("new HSSFSheet @ "+(sheets+1));
//	// }
//	//
//	// if (wb != null && s != null){
//	// if (debug){
//	// System.out.println("Sheets: "+wb.getNumberOfSheets());
//	// System.out.println("HSSF Initialized as: " + filePath );
//	// }
//	// return true;
//	// }
//	//
//	// return false;
//	// } // initializeHSSF()
//
//}
