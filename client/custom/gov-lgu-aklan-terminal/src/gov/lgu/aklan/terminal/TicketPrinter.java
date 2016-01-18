/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.lgu.aklan.terminal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

/**
 *
 * @author wflores
 */
public class TicketPrinter {
    
    private String lguname = "PROVINCIAL GOVT OF AKLAN";
    private String terminalname = "CATICLAN JETTY PORT TERMINAL";
    private String printername = "StarTSP100USB004";
    
    public String getLguname() { return lguname; } 
    public void setLguname(String lguname) {
        this.lguname = lguname; 
    }
    
    public String getTerminalname() { return terminalname; } 
    public void setTerminalname(String terminalname) {
        this.terminalname = terminalname; 
    }    
    
    public String getPrintername() { return printername; } 
    public void setPrintername(String printername) {
        this.printername = printername; 
    }
        
    public byte[] build(List<Map> list) throws Exception { 
        OutputStreamImpl out = new OutputStreamImpl(); 
        out.writeln(new byte[]{27, 64});
        for (Map map : list) {
            Object seqno = map.get("seqno");
            Object gateno = map.get("gateno");
            Object barcode = map.get("barcode");
            Object orno = map.get("orno");
            Object ordate = map.get("ordate");
            Object collector = map.get("collector");
            
            out.alignCenter();
            out.changeFont(16);
            out.writeln(getLguname());
            out.changeFont(8);
            out.writeln(getTerminalname()); 
            out.setLinePaperFeed(1);
            out.changeFont(50);
            out.writeln("TERMINAL PASS"); 
            out.setLinePaperFeed(1);
            out.alignLeft();
            out.changeFont(40);
            out.writeln("Seq #: " + seqno); 
            out.writeln("Gate : " + gateno); 
            out.changeFont(0);

            out.alignCenter();        
            out.setLinePaperFeed(1);
            out.write(0x1d); 
            out.write((int)'H'); 
            out.write(0); 

            //barcode height
            out.setBarcodeHeight(80);
            out.setBarcodeWidth(3);

            //print barcode
            out.write(0x1d);
            out.write((int)'k');
            out.write(4);
            out.write(barcode.toString().getBytes());
            out.write(0);
            out.setLinePaperFeed(1);
            out.alignLeft();
            out.changeFont(0);
            if (orno == null || orno.toString().length() == 0) {
                out.writeln("Date Printed : " + ordate);
                out.writeln("Issued By    : " + collector);
                
            } else {
                out.writeln("OR No.   : " + orno);
                out.writeln("OR Date  : " + ordate);
                out.writeln("Collector: " + collector);
            }

            out.setLinePaperFeed(3);
            out.writeln(new byte[]{29, 86, 66, 1});  //cut
            out.setLinePaperFeed(3);
        }
        return out.toByteArray(); 
    }
    
    public String padLeft(String text, int length, String prefix) {
        StringBuilder sb = new StringBuilder();
        int diff = Math.max(length - text.length(), 0); 
        if (diff > 0) {
            for (int i=0; i<diff; i++) {
                sb.append(prefix);
            }
        }
        sb.append(text);
        return sb.toString(); 
    }
    
    public void print(String name, List<Map> list) throws Exception {
        byte[] bytes = build( list ); 
        print( name, bytes ); 
    }
    
    public void print(String name, byte[] bytes) throws Exception {
        PrinterName printerName = new PrinterName(name, null);
        AttributeSet aset = new HashPrintServiceAttributeSet(printerName);
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, aset); 
        if (printServices.length == 0) {
            throw new IllegalStateException("'" + printerName.getValue() + "' printer not available"); 
        }
        
        DocPrintJob job = printServices[0].createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        job.print(doc, null);             
    } 
    
    private class OutputStreamImpl extends ByteArrayOutputStream {
        byte[] LF = new byte[]{ 0x0a };
        
        public OutputStreamImpl writeln(String str) throws IOException {
            write(str.getBytes()); 
            write(LF); 
            return this;
        }
        
        public OutputStreamImpl writeln(byte[] values) throws IOException {
            write(values); 
            write(LF); 
            return this;
        } 
        
        public OutputStreamImpl writeln(char[] values) throws IOException {
            for (int i=0; i<values.length; i++) {
                write((int) values[i]);
            }
            write(LF); 
            return this;
        }         
        
        public OutputStreamImpl writeln(int value) throws IOException {
            write(value); 
            write(LF); 
            return this;
        }      
        
        public OutputStreamImpl writeln(int[] values) throws IOException {
            for (int i=0; i<values.length; i++) {
                write(values[i]);
            }
            write(LF); 
            return this;
        } 
        
        void alignLeft() {
            write(0x1b); 
            write((int)'a'); 
            write(0);             
        }
        
        void alignCenter() {
            write(0x1b); 
            write((int)'a'); 
            write(1); 
        } 
        
        void changeFont(int size) {
            write(0x1b); 
            write((int)'!'); 
            write(size); 
        }
        
        void setBarcodeHeight(int size) {
            write(0x1d); 
            write((int)'h'); 
            write(size); 
        }
        
        void setBarcodeWidth(int size) {
            write(0x1d); 
            write((int)'w'); 
            write(size); 
        }
        
        void setLinePaperFeed(int size) {
            write(0x1b);
            write((int)'d');
            write(size);
        }
    }    
    
}
