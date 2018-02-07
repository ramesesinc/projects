/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import junit.framework.TestCase;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 *
 * @author ramesesinc
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    public void xtestMain() throws Exception { 
        
        String filepath = "C:\\Users\\ramesesinc\\Desktop\\BILLING09-2017.xls";
        File file = new File( filepath ); 
        System.out.println("canonicalPath-> " + file.getCanonicalPath());
        Workbook wb = Workbook.getWorkbook( file );
        Sheet sheet = wb.getSheet(0); 
        int colsize = sheet.getColumns();
        for (int i=0; i<colsize; i++) {
            Cell cell = sheet.getCell(i, 0);
            System.out.println( cell.getContents());
        }
        System.out.println("colcount=" + sheet.getColumns() );
        System.out.println("rowcount=" + sheet.getRows());
    }
    
    public void test2() throws Exception { 
        String filepath = "C:\\Users\\ramesesinc\\Desktop\\BILLING09-2017.xls";
        filepath = "C:\\TEMP\\etracs25-client-tester\\datafile.encoded";
        File file = new File( filepath ); 
        byte[] bytes = com.rameses.io.IOStream.toByteArray(file); 
        System.out.println(new String(bytes));
        
        Object o = new com.rameses.util.Base64Cipher().decode( new String(bytes));  
        System.out.println( o );
    }    
}
