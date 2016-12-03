/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dell
 */
public class NewClass {
    public static void main(String[] args) {
        String s = "MTOP[pin][yyyy][MM][%06d]";
        Pattern p = Pattern.compile("\\[.*?\\]");
        StringBuilder sb = new StringBuilder();
        Matcher m = p.matcher(s);
        int i = 0;
        try {
            while( m.find(i)) {
                if( m.start() - 1 > i ) {
                    sb.append( s.substring( i, m.start()-i ) );
                }
                String mg = m.group().substring(1, m.group().length()-1 );
                if( mg.equals("pin")) {
                    
                }
                sb.append( "*"+ mg +"*" );
                i = (m.end()-1);
            }
        }
        catch(Exception ign){;}
        if( i < s.length()-1) {
            sb.append( s.substring(i) );
        }
        System.out.println(sb.toString());
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        System.out.println( df.format(new Date()));
        /*
         * 
        while( m.find(i) ) {
            System.out.print(m.group());
            i = m.end() - 1;
        }
        */
    }
}
