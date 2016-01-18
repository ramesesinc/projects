package com.rameses.gov.treasury.tool.statementofreceipts

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFFont
import org.apache.poi.hssf.usermodel.HSSFDataFormat
import org.apache.poi.hssf.util.HSSFColor;
/**
 *
 * @author Dino Quimson
 */
class EsreXlsxExporter {
    
    private JFileChooser fileChooser;
    private int fileChooserValue;
    private String fileLocation;
    private String fileExtention = "";
    public def data;
    public def entity;
    boolean noFileSelected = true;
    private HSSFSheet sheet;
    private HSSFWorkbook workbook;
    def formulaList3;
    def formulaList2;
    def formulaList1;
    
    public void selectFileLocation(){
        fileChooser = new JFileChooser("Select the location");
        fileChooser.addChoosableFileFilter(new XlsxFileFilter());
        fileChooserValue = fileChooser.showSaveDialog(null);
        if(fileChooserValue!=0){
            return;
        }
        if(fileChooserValue==JFileChooser.APPROVE_OPTION){
            if(fileChooser.getFileFilter().getDescription().equals("Excel Workbook (.xls)")){
                fileExtention = ".xls";
            }
            fileLocation = fileChooser.getSelectedFile().getAbsolutePath()+fileExtention;
            noFileSelected = false;
        }
    }  
    
    public String getFileLocation(){
        return fileLocation;
    }
    
    public void export(){
        boolean error = false;
        selectFileLocation();
        if(noFileSelected){
            return;
        }
        
        //loading the contents of the original file to the exported file
        InputStream fileReader = null;
        FileOutputStream fileWriter = null;
        try{
            fileReader = getClass().getResourceAsStream("/com/rameses/gov/treasury/tool/statementofreceipts/esre.xls");
            fileWriter = new FileOutputStream(getFileLocation());
            int filebyte;
            //read the original file
            while((filebyte = fileReader.read())!=-1){
                //write each byte from the original file to the exported file
                fileWriter.write(filebyte);
            }
            fileWriter.close();
            fileReader.close();
        }catch(Exception e){
            error = true;
            showError(e.toString());
        }
        
        //writing data to excel file
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try{
            inputStream = new FileInputStream(new File(getFileLocation()));
            workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheet("10_SRS");
            
            //set the name of the municipality/city/province
            HSSFRow row1 = sheet.getRow((short)4);
            HSSFCell cell1 = row1.getCell((short)2);
            cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue("Municipality: "+entity.lgu);
            
            //set the period covered
            HSSFRow row2 = sheet.getRow((short)5);
            HSSFCell cell2 = row2.getCell((short)2);
            cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell2.setCellValue("Period Covered: Q"+entity.qtr+", "+entity.year);
            
            
            //write each data in the ms excel file
            int rownum = 7;
            formulaList3 = [];
            formulaList2 = [];
            formulaList1 = [];
            data.each{
                //styles and fonts
                HSSFCellStyle particularStyle = workbook.createCellStyle();
                HSSFCellStyle numericStyle = workbook.createCellStyle();
                HSSFDataFormat format = workbook.createDataFormat();
                numericStyle.setDataFormat(format.getFormat("#,###"));

                HSSFFont defaultFont = workbook.createFont();
                defaultFont.setFontName("Arial");
                defaultFont.setFontHeightInPoints((short)8);
                defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont numericFont = workbook.createFont();
                numericFont.setFontName("Arial");
                numericFont.setFontHeightInPoints((short)9);
                numericFont.setItalic(true);
                numericStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                numericStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                numericStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                numericStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                numericStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                numericStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            
                int columnnum = 5;
                if(it.level == 0){
                    columnnum =2;
                    numericFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    numericFont.setItalic(false);
                    numericStyle.setFillForegroundColor(HSSFColor.WHITE.index);
                    numericStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                }
                if(it.level == 1){
                    columnnum =2;
                    numericFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    numericFont.setItalic(false);
                    def entity = [
                        objid       : it.objid,
                        parentid    : it.parentid,
                        row         : rownum,
                        childrenrow : []
                    ];
                    formulaList1 << entity;
                }
                if(it.level == 2){
                    columnnum = 3;
                    numericFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    numericFont.setItalic(false);
                    def entity = [
                        objid       : it.objid,
                        parentid    : it.parentid,
                        row         : rownum,
                        childrenrow : []
                    ];
                    formulaList2 << entity;
                    String parentid = it.parentid;
                    formulaList1.each{
                        if(it.objid == parentid){
                            it.childrenrow << rownum + 1;
                        }
                    }
                }
                if(it.level == 3){
                    columnnum = 4;
                    defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                    def entity = [
                        objid       : it.objid,
                        parentid    : it.parentid,
                        row         : rownum,
                        childrenrow : []
                    ];
                    formulaList3 << entity;
                    String parentid = it.parentid;
                    formulaList2.each{
                        if(it.objid == parentid){
                            it.childrenrow << rownum + 1;
                        }
                    }
                }
                if(it.level == 4){
                    columnnum = 5;
                    defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                    String parentid = it.parentid;
                    formulaList3.each{
                        if(it.objid == parentid){
                            it.childrenrow << rownum + 1;
                        }
                    }
                }
                
                particularStyle.setFont(defaultFont);
                numericStyle.setFont(numericFont);
                               
                //create cell-border
                HSSFRow rowdata = sheet.createRow(rownum);
                for(int i=2; i<=11; i++){
                    HSSFCellStyle style = workbook.createCellStyle();
                    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    HSSFCell celldata = rowdata.createCell((short)i);
                    celldata.setCellStyle(style);
                }
                
                //write account-title to ms excel
                HSSFCell cellParticular = rowdata.createCell((short)columnnum);
                cellParticular.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellParticular.setCellValue(it.account.title);
                cellParticular.setCellStyle(particularStyle);
                
                //write target-value to ms excel
                HSSFCell targetParticular = rowdata.createCell((short)8);
                targetParticular.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                targetParticular.setCellValue(it.target);
                targetParticular.setCellStyle(numericStyle);
                
                //write actual-value to ms excel
                HSSFCell actualParticular = rowdata.createCell((short)9);
                actualParticular.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                actualParticular.setCellValue(it.actual);
                actualParticular.setCellStyle(numericStyle);
                
                //write excess-value to ms excel
                HSSFCell excessParticular = rowdata.createCell((short)10);
                excessParticular.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                excessParticular.setCellValue(it.excess);
                excessParticular.setCellStyle(numericStyle);
                
                //write percentage-value to ms excel
                HSSFCell percentageParticular = rowdata.createCell((short)11);
                percentageParticular.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                percentageParticular.setCellValue(it.percentage);
                percentageParticular.setCellStyle(numericStyle);
                
                rownum++;
            }
            
            //creating formula for the totals
            createESREFormula3();
            createESREFormula2();
            createESREFormula1();
            
            //commit changes to the excel file
            outputStream = new FileOutputStream(getFileLocation());
            workbook.write(outputStream);
            
            inputStream.close();
            outputStream.close();
            
        }catch(Exception e){
            error = true;
            showError(e.toString());
        }
        
        if(!error){
            MsgBox.alert("The file was successfully exported.");
        }
    }
    
    public void createESREFormula3(){
        formulaList3.each{
            String formulaI = getLevel3Formula(it.objid, "I");
            String formulaJ = getLevel3Formula(it.objid, "J");
            
            HSSFCell cellI = getCell(it.row, 8);
            HSSFCell cellJ = getCell(it.row, 9);
            HSSFCell cellK = getCell(it.row, 10);
            HSSFCell cellL = getCell(it.row, 11);
            
            if(formulaI) cellI.setCellFormula(formulaI);
            if(formulaJ) cellJ.setCellFormula(formulaJ);
            cellK.setCellFormula("J"+(it.row+1)+"-"+"I"+(it.row+1));
            cellL.setCellFormula("K"+(it.row+1)+"/"+"I"+(it.row+1));
            
            HSSFFont font = workbook.createFont();
            font.setFontName("Arial");
            font.setItalic(false);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            
            cellI.getCellStyle().setFont(font);
            cellJ.getCellStyle().setFont(font);
        }
    }
    
    public void createESREFormula2(){
        formulaList2.each{
            String formulaI = getLevel2Formula(it.objid, "I");
            String formulaJ = getLevel2Formula(it.objid, "J");
            
            HSSFCell cellI = getCell(it.row, 8);
            HSSFCell cellJ = getCell(it.row, 9);
            HSSFCell cellK = getCell(it.row, 10);
            HSSFCell cellL = getCell(it.row, 11);
            
            if(formulaI) cellI.setCellFormula(formulaI);
            if(formulaJ) cellJ.setCellFormula(formulaJ);
            cellK.setCellFormula("J"+(it.row+1)+"-"+"I"+(it.row+1));
            cellL.setCellFormula("K"+(it.row+1)+"/"+"I"+(it.row+1));
        }
    }
    
    public void createESREFormula1(){
        formulaList1.each{
            String formulaI = getLevel1Formula(it.objid, "I");
            String formulaJ = getLevel1Formula(it.objid, "J");
            
            HSSFCell cellI = getCell(it.row, 8);
            HSSFCell cellJ = getCell(it.row, 9);
            HSSFCell cellK = getCell(it.row, 10);
            HSSFCell cellL = getCell(it.row, 11);
            
            if(formulaI) cellI.setCellFormula(formulaI);
            if(formulaJ) cellJ.setCellFormula(formulaJ);
            cellK.setCellFormula("J"+(it.row+1)+"-"+"I"+(it.row+1));
            cellL.setCellFormula("K"+(it.row+1)+"/"+"I"+(it.row+1));
        }
    }
    
    public HSSFCell getCell(int row, int column){
        HSSFRow rowdata = sheet.getRow((short)row);
        HSSFCell cell = rowdata.getCell((short)column);
        return cell;
    }
    
    public String getLevel3Formula(String objid, String column){
        String formula = null;
        String cell1 = null;
        String cell2 = null;
        formulaList3.each{
            if(it.objid == objid){
                it.childrenrow.each{rownum ->
                    if(!cell1) cell1 = column + rownum;
                    cell2 = column + rownum;
                }
            }
        }
        if(cell1 != null && cell2!= null) formula = "SUM("+cell1+":"+cell2+")";
        return formula;
    }
    
    public String getLevel2Formula(String objid, String column){
        String formula = "";
        formulaList2.each{
            if(it.objid == objid){
                it.childrenrow.each{rownum ->
                    formula += column + rownum + ",";
                }
            }
        }
        if(formula != ""){
            formula = "SUM(" + formula.substring(0,formula.length()-1) + ")";
        }
        return formula;
    }
    
    public String getLevel1Formula(String objid, String column){
        String formula = "";
        formulaList1.each{
            if(it.objid == objid){
                it.childrenrow.each{rownum ->
                    formula += column + rownum + ",";
                }
            }
        }
        if(formula != ""){
            formula = "SUM(" + formula.substring(0,formula.length()-1) + ")";
        }
        return formula;
    }
    
    public void showError(String error){
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
   
}

