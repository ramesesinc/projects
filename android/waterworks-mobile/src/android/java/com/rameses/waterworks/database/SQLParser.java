/*
 * SQLParser.java
 *
 * Created on January 28, 2014, 1:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.waterworks.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wflores 
 */
class SQLParser 
{
    private List<String> parameterNames; 
    private String sqlString;
    
    SQLParser() {
        parameterNames = new ArrayList(); 
        sqlString = null; 
    }
    
    void parse(String text, Map params) {
        sqlString = null;         
        parameterNames.clear();
        if (text == null || text.length() == 0) return;
        
        text = parseGlobalExpression(text, params); 
        sqlString = parseParamExpression(text); 
    }

    public String getSql() { 
        return sqlString; 
    } 
    
    public String[] getParameterNames() {
        return parameterNames.toArray(new String[]{});
    }
    
    private String parseGlobalExpression(String text, Map params) {
        int length = text.length();
        int start  = 0;        
        StringBuffer buffer = new StringBuffer();        
        Pattern pattern = Pattern.compile("\\$\\{[a-zA-Z0-9_.]{1,}\\}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int idx0 = matcher.start();
            int idx1 = matcher.end();
            buffer.append(text.substring(start, idx0));
            
            String group = matcher.group();
            String name  = group.substring(2, group.length()-1); 
            Object value = (params == null? null: params.get(name)); 
            buffer.append(value == null? "": value.toString());
            start = idx1;
        }
        if (start < length) {
            buffer.append(text.substring(start));
        }
        return buffer.toString(); 
    }    
    
    private String parseParamExpression(String text) {
        int length = text.length();
        int start  = 0;        
        StringBuffer buffer = new StringBuffer();        
        Pattern pattern = Pattern.compile("\\$P\\{[a-zA-Z0-9_.]{1,}\\}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int idx0 = matcher.start();
            int idx1 = matcher.end();
            buffer.append(text.substring(start, idx0));
            buffer.append("?");
            
            String group = matcher.group();
            String name  = group.substring(3, group.length()-1); 
            parameterNames.add(name); 
            start = idx1;
        }
        if (start < length) {
            buffer.append(text.substring(start));
        }
        return buffer.toString(); 
    }    
}
