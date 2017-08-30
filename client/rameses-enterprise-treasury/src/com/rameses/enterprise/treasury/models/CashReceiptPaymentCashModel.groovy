package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.math.BigDecimal; 

class CashReceiptPaymentCashModel  { 

    def entity;

    def cash = 0.0;
    def change = 0.0;
    def saveHandler;
    def page = "default";
    

    def toDecimal( value ) {
        if ( value == null ) return value; 

        def df = new java.text.DecimalFormat("0.00"); 
        df.setRoundingMode( java.math.RoundingMode.HALF_UP ); 

        def num = null; 
        if ( value instanceof Number ) {
            num = value; 
        } else { 
            num = new java.math.BigDecimal( value.toString() );  
        } 
        return new java.math.BigDecimal( df.format(num) ); 
    } 
    
    def doOk() {
        if( page == "default") {
            cash = toDecimal( cash ); 
            if(cash<=0)
             throw new Exception("Cash amount must be greater than 0");
             
            def amtdue = entity.amount - entity.totalnoncash;
            if ( amtdue < cash && amtdue > 0.0 ) { 
                change = cash - amtdue; 
            } else { 
                change = 0.0; 
            } 
            if(change>0) {
                page = "change";
                return "change";
            }
            else {
                saveHandler( [cash:cash, change:change] );
                return "_close";
            }
        }
        else {
            saveHandler( [cash:cash, change:change] );
            return "_close";
        }
    }
    
    def doCancel() {
        if( page == "change" ) {
            page = "default";
            return page;
        }
        else {
            return "_close";
        }
    }
    
    
} 