package com.rameses.waterworks.bean;

public class ItemAccount {
    
    private String account;
    private double amount;
    
    public ItemAccount(String account, double amount){
        this.account = account;
        this.amount = amount;
    }
    
    public String getAccount(){ return account; }
    public double getAmount(){ return amount; }
    
}
