/*
 * NumberFunc.java
 *
 * Created on July 18, 2014, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.clfc.functions;

import java.math.BigDecimal;

/**
 *
 * @author louie
 */
public final class ModifiedNumberFunc {
    
    public static BigDecimal roundOffNearest5Cents(BigDecimal number) {
        String n = number.toString();
        char c = n.charAt(n.length()-1);
        int ld = Integer.parseInt(c+"");
        BigDecimal result = new BigDecimal("0").setScale(2);
        if (ld > 0 && ld < 5) {
            result = new BigDecimal(n.substring(0, n.length()-1)+'5');            
        } else if (ld > 5) {
            String a = "0.0" + (10-ld);
            result = new BigDecimal(n).setScale(2);
            result = result.add(new BigDecimal(a).setScale(2));
        } else {
            result = new BigDecimal(n).setScale(2);
        }
        return result;
    }
    
}
