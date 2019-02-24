package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class BillingReceiptModel {
    
    def item;   // set by the caller 
    def caller; // set by the caller 
    
    @Service("WaterworksBatchBillPrintingService")
    def printSvc;
    
    def text;
    
    public def init() {
        def options = [previewOnly: true, acctno: item.acctno]; 
        def onprint = { printerName, value-> 
            text = value; 
        } 
        caller.printBillImpl( options, onprint ); 
        return (text ? null : '_close'); 
    } 
}