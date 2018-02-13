/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import java.util.Date;
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
        String str = "2018-02-12 12:32:00.0";
        
        Date dt = java.sql.Timestamp.valueOf( str );
        System.out.println( dt );
    }    
}
