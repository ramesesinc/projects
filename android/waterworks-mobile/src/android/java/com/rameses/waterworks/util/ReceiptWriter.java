/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.waterworks.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
public class ReceiptWriter {
    
    private final byte[] LF = new byte[]{ 0x0a }; 
    
    private ByteArrayOutputStream out; 
        
    public byte[] getBytes() { 
        return (out == null? null: out.toByteArray()); 
    } 
    
    public void init() { 
        try { out.close(); } catch(Throwable t){;} 
        
        out = new ByteArrayOutputStream(); 
        writeln(new byte[]{27, 64});
    } 

    public void close() {
        try { out.close(); } catch(Throwable t){;} 
    }
    
    public void writeln( String str ) {
        try {
            out.write(str.getBytes()); 
            out.write(LF); 
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex); 
        }        
    }
    
    public void writeln( byte[] values ) {
        try {
            out.write(values); 
            out.write(LF); 
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex); 
        }        
    }   
    
    public void writeln( char[] values ) {
        try {
            for (int i=0; i<values.length; i++) {
                out.write((int) values[i]);
            }
            out.write(LF); 
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex); 
        }        
    }      
    
    public void writeln( int value ) {
        try {
            out.write(value); 
            out.write(LF); 
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex); 
        }        
    }     
    
    public void writeln( int[] values ) {
        try {
            for (int i=0; i<values.length; i++) {
                out.write(values[i]);
            }
            out.write(LF);  
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex); 
        }        
    }    
    
    public void writeBarcode( String value ) { 
        byte[] bytes = value.getBytes();
        
        try {
            out.write(0x1d);
            out.write((int)'k');
            out.write(4);
            out.write( bytes );
            out.write(0);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex); 
        }
    }    
    
    public void alignLeft() { 
        out.write(0x1b); 
        out.write((int)'a'); 
        out.write(0);  
    }
    
    public void alignCenter() {
        out.write(0x1b); 
        out.write((int)'a'); 
        out.write(1); 
    } 

    public void changeFont(int size) {
        out.write(0x1b); 
        out.write((int)'!'); 
        out.write(size); 
    }

    public void setBarcodeHeight(int size) {
        out.write(0x1d); 
        out.write((int)'h'); 
        out.write(size); 
    }
    
    public void setBarcodeWidth(int size) {
        out.write(0x1d); 
        out.write((int)'w'); 
        out.write(size); 
    } 
    
    public void setBarcodeSize( int width, int height ) {
        setBarcodeWidth( width ); 
        setBarcodeHeight( height ); 
    }    
    
    public void setLinePaperFeed(int size) {
        out.write(0x1b);
        out.write((int)'d');
        out.write(size);
    } 
    
    public void cutPaper() { 
        writeln(new byte[]{29, 86, 66, 1});  //cut        
    }
    
    public void print( String name ) throws Exception { 
        PrinterName printerName = new PrinterName(name, null);
        AttributeSet aset = new HashPrintServiceAttributeSet(printerName);
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, aset); 
        if (printServices.length == 0) {
            throw new IllegalStateException("'" + printerName.getValue() + "' printer not available"); 
        }

        DocPrintJob job = printServices[0].createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc( out.toByteArray(), flavor, null ); 
        job.print(doc, null); 
    } 
    
    public void print( OutputStream target ) {
        try { 
            target.write( getBytes() ); 
            target.flush();
        } catch (IOException ex) { 
            throw new RuntimeException(ex.getMessage(), ex); 
        } finally {
            try { target.close(); }catch(Throwable t){;} 
        }
    } 
}
