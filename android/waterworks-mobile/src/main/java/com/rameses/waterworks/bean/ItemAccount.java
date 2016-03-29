package com.rameses.waterworks.bean;

import java.text.DecimalFormat;

public class ItemAccount {
    
    private String account;
    private double amount;
    
    public ItemAccount(String account, double amount){
        this.account = account;
        this.amount = amount;
    }
    
    public String getAccount(){ return account; }
    
    public String getAmount(){ 
        return format(amount);
    }
    
    public static String format(double amount){
        DecimalFormat df = new DecimalFormat("0.00");
        String val = df.format(amount);
        int strlen = val.length();
        String prefix = "";
        for(int x=strlen; x <= 5; x++){
            prefix += " ";
        }
        return prefix + val; 
    }
    
}
