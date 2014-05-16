package org.qedsys.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelFileReader {
    public static void main(String[] args){}

    public ExcelFileReader(){}
    private static final Logger LOG = LoggerFactory.getLogger(ExcelFileReader.class);

    Cell[][] cellArray;
    FileInputStream fileInputStream;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    Date cellDate;
    String cellString;
    double cellDouble;
    boolean cellBoolean;
    String cellFormula;
    int rowCount=0;
    int colCount=0;

    public void setFile(String fileName) throws IOException{
        fileInputStream = new FileInputStream(fileName);
        workbook = new XSSFWorkbook(fileInputStream);
        LOG.info("Excel file set to " + fileName);
    }

    public void setSheet(String sheetName){
        sheet = workbook.getSheet(sheetName);
        LOG.info("Sheet is set to " + sheetName);
    }
   
    public String getArrayValue(String[] array, int value){
        return array[value-1];
    }

    public Cell[][] setArrayDimensions() throws IOException{
        for (Row row : sheet) {
            rowCount++;
            for (Cell cell : row) {
                if(cell!=null){
                    colCount++;
                }
                else
                    break;
            }
        }
        return new Cell[rowCount][colCount/rowCount];
    }

    public Cell[][] readExcel() throws IOException{
        for (Row row : sheet) {
            for (Cell cell : row) {
                if(cell!=null){
                    cellArray[row.getRowNum()][cell.getColumnIndex()] = cell;
                    LOG.info("Row:" + row.getRowNum() + " Column:" + cell.getColumnIndex() + " Content:" + cell);
                }
                else break;
            }
        }
        return cellArray;
    }

    public String getString(){
        return cellString;
    }

    public double getDouble(){
        return cellDouble;
    }

    public boolean getBoolean(){
        return cellBoolean;
    }

    public Date getDate(){
        return cellDate;
    }

    public String getFormula(){
        return cellFormula;
    }
   
    public String getLeftHeader(){
        Header leftHeader = sheet.getHeader();
        return leftHeader.getLeft();
    }
   
    public String getCenterHeader(){
        Header centerHeader = sheet.getHeader();
        return centerHeader.getCenter();
    }
   
    public String getRightHeader(){
        Header rightHeader = sheet.getHeader();
        return rightHeader.getRight();
    }
   
    public String getLeftFooter(){
        Footer leftFooter = sheet.getFooter();
        return leftFooter.getLeft();
    }
   
    public String getCenterFooter(){
        Footer centerFooter = sheet.getFooter();
        return centerFooter.getCenter();
    }
   
    public String getRightFooter(){
        Footer rightFooter = sheet.getFooter();
        return rightFooter.getRight();
    }
   
    public String[] getStringArrayFromFileWithSheetForRow(String fileName, String sheetName, int row) throws IOException{
        int arrayCounter = 0;
        int arraySize = 0;
        setFile(fileName);
        setSheet(sheetName);
        cellArray = setArrayDimensions();
        readExcel();
        arraySize = (colCount/rowCount);
        System.out.println(arraySize);
        String[] arrayString = new String[arraySize];
        int i = row-1;
        for (int j = 0; j < (colCount/rowCount); j++) {
            switch (cellArray[i][j].getCellType()) {
            case Cell.CELL_TYPE_STRING:
                arrayString[arrayCounter] = cellArray[i][j].getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cellArray[i][j])) {
                    arrayString[arrayCounter] = cellArray[i][j].getDateCellValue().toString();
                } else {
                    arrayString[arrayCounter] = String.valueOf(cellArray[i][j].getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                arrayString[arrayCounter] = String.valueOf(cellArray[i][j].getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                arrayString[arrayCounter] = cellArray[i][j].getCellFormula();
                break;   
            }
            arrayCounter++;
        }
        return arrayString;
    }
   
    public String[] setCellArray() throws IOException{
        cellArray = setArrayDimensions();
        readExcel();
        int arrayCounter = 0;
        int arraySize = rowCount * (colCount/rowCount);
        String[] arrayString = new String[arraySize];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount/rowCount; j++) {
                switch (cellArray[i][j].getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    arrayString[arrayCounter] = cellArray[i][j].getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cellArray[i][j])) {
                        arrayString[arrayCounter] = cellArray[i][j].getDateCellValue().toString();
                    } else {
                        arrayString[arrayCounter] = String.valueOf(cellArray[i][j].getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    arrayString[arrayCounter] = String.valueOf(cellArray[i][j].getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    arrayString[arrayCounter] = cellArray[i][j].getCellFormula();
                    break;   
                }
                arrayCounter++;
            }

        }
        return arrayString;
    }

    public String[] getStringArrayFromFileWithSheet(String fileName, String sheetName) throws IOException{
        int arrayCounter = 0;
        int arraySize = 0;
        setFile(fileName);
        setSheet(sheetName);
        cellArray = setArrayDimensions();
        readExcel();
        arraySize = rowCount * (colCount/rowCount);
        String[] arrayString = new String[arraySize];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount/rowCount; j++) {
                switch (cellArray[i][j].getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    arrayString[arrayCounter] = cellArray[i][j].getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cellArray[i][j])) {
                        arrayString[arrayCounter] = cellArray[i][j].getDateCellValue().toString();
                    } else {
                        arrayString[arrayCounter] = String.valueOf(cellArray[i][j].getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    arrayString[arrayCounter] = String.valueOf(cellArray[i][j].getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    arrayString[arrayCounter] = cellArray[i][j].getCellFormula();
                    break;   
                }
                arrayCounter++;
            }

        }
        return arrayString;
    }
   
    public String getCell(int row, int col){
        String cellValue;
        if(cellArray[row-1][col-1].getCellType()==Cell.CELL_TYPE_NUMERIC || cellArray[row-1][col-1].getCellType()==Cell.CELL_TYPE_BOOLEAN || cellArray[row-1][col-1].getCellType()==Cell.CELL_TYPE_FORMULA || cellArray[row-1][col-1].getCellType()==Cell.CELL_TYPE_BLANK){
            cellValue = cellArray[row-1][col-1].toString();
        }
        else{
        cellValue = cellArray[row-1][col-1].getStringCellValue();
        }
        /*switch (cellArray[row-1][col-1].getCellType()) {
            case Cell.CELL_TYPE_STRING:
                    cellString = cellArray[row-1][col-1].getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cellArray[row-1][col-1])) {
                           cellString = cellArray[row-1][col-1].getDateCellValue().toString();
                    } else {
                           cellString = String.valueOf(cellArray[row-1][col-1].getNumericCellValue());
                    }
            case Cell.CELL_TYPE_BOOLEAN:
                    cellString = String.valueOf(cellArray[row-1][col-1].getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                    cellString = cellArray[row-1][col-1].getCellFormula();    
        }
    
        return getString();
           */
        return cellValue;
    }
}