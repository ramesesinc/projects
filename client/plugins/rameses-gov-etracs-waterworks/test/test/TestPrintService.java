/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rameses.io.IOStream;
import com.rameses.osiris2.report.PrinterService;
import java.net.URL;
import javax.swing.JOptionPane;
import junit.framework.TestCase;

/**
 *
 * @author ramesesinc
 */
public class TestPrintService extends TestCase {
    
    public TestPrintService(String testName) {
        super(testName);
    }

    public void test01() throws Exception {
        PrinterService ps = new PrinterService();
        //System.out.println( ps.getPrinters() );
        
        EscPOSBuilder u = new EscPOSBuilder(); 
        u.init();
        u.PrintMode.setCondensed_On(); 

        URL url = getClass().getResource("sample_template_2.txt"); 
        byte[] bytes = IOStream.toByteArray(url); 
        u.append( new String(bytes)); 
        
//        u.PrintMode.setCharacterFontB_CPI_12(); 
//
//        u.setBold_On();
//        u.setDoubleWidth_On();
//        u.append("TRINIDAD WATERWORKS SYSTEM"); 
//        u.setDoubleWidth_Off();
//        u.setBold_Off(); 
//        
//        u.append("\n").append("Municipality of Trinidad, Bohol");
//        
//        u.setBold_On();
//        u.append("\n").append("WATER BILL");
//        u.setBold_Off();
//        
//        
//        u.append("Account No.:");
//        u.carriageReturn();
//        u.setDoubleHeight_On();
//        u.append("____________");
//        u.setDoubleHeight_Off();
        
        u.formFeed();
        u.init();
        
        String printerName = "epson-lq300";
        ps.printString( printerName, u.getText());
        
        JOptionPane.showMessageDialog(null, "Printing...");
    }
    
}
