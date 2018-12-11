package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.report.*;

class PrintAccountLedger {
    
    @Service('WaterworksAccountLedgerPrintingService') 
    def printSvc; 
    
    def entity;
    
    def printerService = new PrinterService();
    
   def getPrinterList() {
       return printerService.getPrinters(); 
   }    
    
    void printLedger() { 
        def printerName = null; 
        boolean pass = false;
        def h = [:];
        h.handler = { v->
            printerName = v.printername; 
            pass = true; 
        } 
        h.fields = [];
        h.fields << [name:"printername", caption:'Select Printer', type:'combo', required:true, itemsObject:getPrinterList() ];
        Modal.show("dynamic:form", h, [title: 'Print Ledger'] );
        if ( !pass ) return; 
        
        def res = printSvc.getReport([ acctid: entity.objid ]); 
        if ( res ) {
            printerService.printString(printerName, res.toString());
            MsgBox.alert("Successfully submitted to the printer");
        } else {
            MsgBox.alert("No available template handler for this report"); 
        }
    } 
    
}