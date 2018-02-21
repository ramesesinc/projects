package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
        
public class CashBreakdownModel extends CrudFormModel {
    
    def total = 0;
    def cashremaining = 0;
    def cashbreakdown = [];
    boolean editable = true;
    def handler;
    
    void init() {
        def arr = [];
        arr.addAll( cashbreakdown );
        cashbreakdown = arr;
    }
    
    def doOk() {
        def numformat = new java.text.DecimalFormat('0.00');          
        def ntotalcash = new java.math.BigDecimal( numformat.format( total ));
        
        def breakdown = 0.0;
        if( cashbreakdown ) {
            breakdown = cashbreakdown.sum{ it.amount } 
            breakdown = new java.math.BigDecimal( numformat.format( breakdown )); 
        }
        def diff = ( ntotalcash - breakdown );
        if ( diff != 0 ) throw new Exception("Cash breakdown must equal total cash");
        handler( [total:total, cashbreakdown: cashbreakdown ] );
        return "_close";
    }
    
    def doCancel() {    
        return "_close";
    }
    
}       