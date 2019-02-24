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
    
    def getPrinterInfo() {
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
        if ( !pass ) return null; 
        return [name: printerName];
    }
    
    def getReportData() {
        return printSvc.getReport([ acctid: entity.objid ]); 
    }
    
    void printLedger() { 
        def res = getReportData(); 
        if ( res ) {
            def printerInfo = getPrinterInfo();
            def printerName = printerInfo?.name;
            if ( !printerName ) return;
            
            printerService.printString(printerName, res.toString());
            MsgBox.alert("Successfully submitted to the printer");
        } else {
            MsgBox.alert("No available template handler for this report"); 
        }
    } 
    
    def text;
    
    public def previewLedger() {
        text = null; 
        def res = getReportData(); 
        if ( res ) {
            text = res.toString(); 
            return 'preview';
        } else {
            MsgBox.alert("No available template handler for this report"); 
            return '_close'; 
        }        
    }
}