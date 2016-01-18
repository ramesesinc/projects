package ocr;

import java.io.*;
import net.sourceforge.tess4j.*;

public class Main{
    
    public static void main(String[] args) throws IOException{
        File imageFile = new File("kk.jpg");
        ITesseract instance = new Tesseract(); // JNA Interface Mapping

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println("Error: "+e);
        }
    }
}
