package com.vieking.sys.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.annotations.BatchSize;

import com.vieking.basicdata.model.Department;
import com.vieking.basicdata.model.Dictionary;
import com.vieking.functions.model.Contact;
import com.vieking.functions.model.ContactPost;
import com.vieking.role.model.User;

public class ExcelUtils {
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY = "";
	public static final String POINT = ".";
	public static final String LIB_PATH = "lib";
	public static final String STUDENT_INFO_XLS_PATH = LIB_PATH
			+ "/student_info" + POINT + OFFICE_EXCEL_2003_POSTFIX;
	public static final String STUDENT_INFO_XLSX_PATH = LIB_PATH
			+ "/student_info" + POINT + OFFICE_EXCEL_2010_POSTFIX;
	public static final String NOT_EXCEL_FILE = " : Not the Excel file!";
	public static final String PROCESSING = "Processing...";
	
	/**
     * read the Excel file
     * @param path the path of the Excel file
     * @return
     * @throws IOException
     */
//    public List<Student> readExcel(String path) throws IOException {
//        if (path == null || Common.EMPTY.equals(path)) {
//            return null;
//        } else {
//            String postfix = Util.getPostfix(path);
//            if (!Common.EMPTY.equals(postfix)) {
//                if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
//                    return readXls(path);
//                } else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
//                    return readXlsx(path);
//                }
//            } else {
//                System.out.println(path + Common.NOT_EXCEL_FILE);
//            }
//        }
//        return null;
//    }
	
	public static void main(String[] args) {
		try {
			String file = "e:/1.xlsx";
//			readXlsx(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

    /**
     * Read the Excel 2010
     * @param path the path of the excel file
     * @return
     * @throws IOException
     */
    public static List<String[]> readXlsx(InputStream is) throws Exception{
//        System.out.println(PROCESSING + path);
//        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        
        List<String[]> list = new ArrayList<String[]>();
        
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(2);
        // Read the Row
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                
                XSSFCell cell1 = xssfRow.getCell(0);
                XSSFCell cell2 = xssfRow.getCell(1);
                XSSFCell cell3 = xssfRow.getCell(2);
//                XSSFCell cell4 = xssfRow.getCell(3);
                XSSFCell cell5 = xssfRow.getCell(4);
                XSSFCell cell6 = xssfRow.getCell(5);
                XSSFCell cell7 = xssfRow.getCell(6);
                XSSFCell cell8 = xssfRow.getCell(7);
                XSSFCell cell9 = xssfRow.getCell(8);
                
                String name = getValue(cell1);
                String phone = getValue(cell2);
                String sex = getValue(cell3);
//                String bm = getValue(cell4);
                String level = getValue(cell5);
                String telephone = getValue(cell6);
                String post = getValue(cell7);
                String email = getValue(cell8);
                String jianjie = getValue(cell9);
                
                
                String[] strArr = new String[9];
                strArr[0] = name;
                strArr[1] = phone;
                strArr[2] = sex;
//                strArr[3] = bm;
                strArr[4] = level;
                strArr[5] = telephone;
                strArr[6] = post;
                strArr[7] = email;
                strArr[8] = jianjie;
                
                if (!phone.trim().equals("")) {
                	list.add(strArr);
                	System.out.println(rowNum + "\t" + name + "\t\t" + phone + "\t" + sex + "\t" + level + "\t" + post + "\t" + email + "\t" + jianjie);
				}
                
                
            	
                
                
            }
        }
        
        return list;
        // Read the Sheet
//        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
//            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
//            if (xssfSheet == null) {
//                continue;
//            }
//            // Read the Row
//            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
//                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
//                if (xssfRow != null) {
//                    
//                    XSSFCell cell1 = xssfRow.getCell(0);
//                    XSSFCell cell2 = xssfRow.getCell(1);
//                    XSSFCell cell3 = xssfRow.getCell(2);
//                    XSSFCell cell4 = xssfRow.getCell(3);
//                    
//                }
//            }
//        }
//        return list;
    }

    /**
     * Read the Excel 2003-2007
     * @param path the path of the Excel
     * @return
     * @throws IOException
     */
//    public List<Student> readXls(String path) throws IOException {
//        System.out.println(Common.PROCESSING + path);
//        InputStream is = new FileInputStream(path);
//        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
//        Student student = null;
//        List<Student> list = new ArrayList<Student>();
//        // Read the Sheet
//        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
//            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
//            if (hssfSheet == null) {
//                continue;
//            }
//            // Read the Row
//            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
//                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
//                if (hssfRow != null) {
//                    student = new Student();
//                    HSSFCell no = hssfRow.getCell(0);
//                    HSSFCell name = hssfRow.getCell(1);
//                    HSSFCell age = hssfRow.getCell(2);
//                    HSSFCell score = hssfRow.getCell(3);
//                    student.setNo(getValue(no));
//                    student.setName(getValue(name));
//                    student.setAge(getValue(age));
//                    student.setScore(Float.valueOf(getValue(score)));
//                    list.add(student);
//                }
//            }
//        }
//        return list;
//    }

    @SuppressWarnings("static-access")
    private static String getValue(XSSFCell xssfRow) {
    	if(xssfRow == null){
    		return "";
    	}
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
        	BigDecimal bd = new BigDecimal(xssfRow.getNumericCellValue()); 
        	String str = bd.toPlainString();
            return str;
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }

//    @SuppressWarnings("static-access")
//    private String getValue(HSSFCell hssfCell) {
//        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
//            return String.valueOf(hssfCell.getBooleanCellValue());
//        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
//            return String.valueOf(hssfCell.getNumericCellValue());
//        } else {
//            return String.valueOf(hssfCell.getStringCellValue());
//        }
//    }

}
