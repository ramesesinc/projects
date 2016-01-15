/*
 * Base64Cipher.java
 *
 * Created on June 5, 2014, 1:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author wflores
 */
public final class Base64Cipher
{
    private static final String REG_EXP = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"; 
    
    // The line separator string of the operating system.
    private static final String systemLineSeparator = System.getProperty("line.separator");
    
    // Mapping table from 6-bit nibbles to Base64 characters.
    private static final char[] map1 = new char[64];
    static 
    {
        int i=0;
        for (char c='A'; c<='Z'; c++) map1[i++] = c;
        for (char c='a'; c<='z'; c++) map1[i++] = c;
        for (char c='0'; c<='9'; c++) map1[i++] = c;
        map1[i++] = '+'; map1[i++] = '/'; 
    }
    
    // Mapping table from Base64 characters to 6-bit nibbles.
    private static final byte[] map2 = new byte[128];
    static 
    {
        for (int i=0; i<map2.length; i++) map2[i] = -1;
        for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; 
    }
    
    public Base64Cipher() {
    }
    
    public boolean isEncoded(char[] value) {
        return isEncoded(new String(value)); 
    }
    
    public boolean isEncoded(String value) {
        return value.matches(REG_EXP); 
    }
    
    public String encode(Object value) {
        if (value == null) return null;

        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value); 
            byte[] bytes = baos.toByteArray();
            return encode(bytes); 
        } catch(RuntimeException re) {
            throw re; 
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e); 
        } finally {
            try { oos.close(); }catch(Throwable t) {;} 
            try { oos.close(); }catch(Throwable t) {;} 
        }        
    }
    
    public String encode(String value) {
        if (value == null) return null; 
        if (isEncoded(value)) return value; 
        
        return encode(value.getBytes()); 
    } 
    
    public String encode(byte[] bytes) {
        char[] chars = encode(bytes, 0, bytes.length); 
        return new String(chars); 
    }
    
    public String encodeString(String s) {
        return new String(encode(s.getBytes())); 
    }    
    
    public String encodeLines(byte[] in) {
        return encodeLines(in, 0, in.length, 76, systemLineSeparator); 
    }
    
    public String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
        int blockLen = (lineLen*3) / 4;
        if (blockLen <= 0) throw new IllegalArgumentException();

        int lines = (iLen+blockLen-1) / blockLen;
        int bufLen = ((iLen+2)/3)*4 + lines*lineSeparator.length();
        StringBuilder buf = new StringBuilder(bufLen);
        int ip = 0;
        while (ip < iLen) {
            int l = Math.min(iLen-ip, blockLen);
            buf.append(encode(in, iOff+ip, l));
            buf.append(lineSeparator);
            ip += l; 
        }
        return buf.toString(); 
    }    
    
    public Object decode(Object value) {
        if (value == null) {
            return value; 
        } else if (value instanceof String) {
            return decode(value.toString()); 
        } else if (value instanceof byte[]) {
            return decode((byte[]) value); 
        } else if (value instanceof char[]) {
            return decode((char[]) value);
        } else {
            return value; 
        }
    }
    
    public Object decode(String value) {
        if (value == null) return null; 
        if (!isEncoded(value)) return value; 
        
        return decode(value.toCharArray()); 
    }
    
    public Object decode(char[] chars) {
        if (!isEncoded(chars)) return chars; 
        
        byte[] bytes = decode(chars, 0, chars.length); 
        return decode(bytes);              
    }
    
    public Object decode(byte[] bytes) {
        if (bytes == null) return null;
        
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes); 
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch(RuntimeException re) {
            throw re; 
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e); 
        } finally {
            try { ois.close(); }catch(Throwable t){;} 
            try { bais.close(); }catch(Throwable t){;} 
        }            
    }
    
    public byte[] decodeLines(String s) {
        char[] buf = new char[s.length()];
        int p = 0;
        for (int ip = 0; ip < s.length(); ip++) {
            char c = s.charAt(ip); 
            if (c != ' ' && c != '\r' && c != '\n' && c != '\t') { 
                buf[p++] = c; 
            } 
        } 
        return decode(buf, 0, p); 
    }    
    
    
        
    /**
     * Encodes a byte array into Base64 format.
     * No blanks or line breaks are inserted in the output.
     * @param in    An array containing the data bytes to be encoded.
     * @param iOff  Offset of the first byte in <code>in</code> to be processed.
     * @param iLen  Number of bytes to process in <code>in</code>, starting at <code>iOff</code>.
     * @return      A character array containing the Base64 encoded data.
     */    
    public char[] encode(byte[] in, int iOff, int iLen) {
        int oDataLen = (iLen*4+2)/3;       // output length without padding
        int oLen = ((iLen+2)/3)*4;         // output length including padding
        char[] out = new char[oLen];
        int ip = iOff;
        int iEnd = iOff + iLen;
        int op = 0;
        while (ip < iEnd) 
        {
            int i0 = in[ip++] & 0xff;
            int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
            int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
            int o0 = i0 >>> 2;
            int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
            int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
            int o3 = i2 & 0x3F;
            out[op++] = map1[o0];
            out[op++] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '='; op++;
            out[op] = op < oDataLen ? map1[o3] : '='; op++; 
        }
        return out; 
    }    
    
    /**
     * Decodes a byte array from Base64 format.
     * No blanks or line breaks are allowed within the Base64 encoded input data.
     * @param in    A character array containing the Base64 encoded data.
     * @param iOff  Offset of the first character in <code>in</code> to be processed.
     * @param iLen  Number of characters to process in <code>in</code>, starting at <code>iOff</code>.
     * @return      An array containing the decoded data bytes.
     * @throws      IllegalArgumentException If the input is not valid Base64 encoded data.
     */    
    public byte[] decode(char[] in, int iOff, int iLen) { 
        if (iLen%4 != 0) throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        while (iLen > 0 && in[iOff+iLen-1] == '=') iLen--;
        int oLen = (iLen*3) / 4;
        byte[] out = new byte[oLen];
        int ip = iOff;
        int iEnd = iOff + iLen;
        int op = 0;
        while (ip < iEnd) 
        {
            int i0 = in[ip++];
            int i1 = in[ip++];
            int i2 = ip < iEnd ? in[ip++] : 'A';
            int i3 = ip < iEnd ? in[ip++] : 'A';
            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");

            int b0 = map2[i0];
            int b1 = map2[i1];
            int b2 = map2[i2];
            int b3 = map2[i3];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");

            int o0 = ( b0       <<2) | (b1>>>4);
            int o1 = ((b1 & 0xf)<<4) | (b2>>>2);
            int o2 = ((b2 &   3)<<6) |  b3;
            out[op++] = (byte)o0;
            if (op<oLen) out[op++] = (byte)o1;
            if (op<oLen) out[op++] = (byte)o2; 
        } 
        return out; 
    }  
}

