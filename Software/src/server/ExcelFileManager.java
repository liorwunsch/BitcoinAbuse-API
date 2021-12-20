package server;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import common.ReportEntry;

public class ExcelFileManager {

	// https://www.javatpoint.com/java-create-excel-file
	public boolean writeToExcelFile(String save_path, ArrayList<ReportEntry> report_entry_list) {
		try {
			String timeStamp = new SimpleDateFormat("dd-MM-yy_HH-mm-ss").format(Calendar.getInstance().getTime());
			String file_name = save_path + "/Report_Analysis_" + timeStamp + ".xls";

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Report_Analysis");

			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Address");
			rowhead.createCell(1).setCellValue("Report Count");
			rowhead.createCell(2).setCellValue("Link");

			int j = 1;
			for (ReportEntry entry : report_entry_list) {
				HSSFRow row = sheet.createRow((short) j);
				row.createCell(0).setCellValue(entry.getAddress());
				row.createCell(1).setCellValue(Integer.valueOf(entry.getReportCount()));
				row.createCell(2).setCellValue(entry.getLink().getText());
				j++;
			}

			for (int i = 0; i < 2; i++) {
				sheet.autoSizeColumn(i);
			}

			FileOutputStream file_out = new FileOutputStream(file_name);
			workbook.write(file_out);
			file_out.close();
			workbook.close();

			System.out.println("Excel file has been generated successfully.");
			return true;
		} catch (Exception e) {
			System.err.println("An error occurred while writing to Excel file.");
			return false;
		}
	}
}
