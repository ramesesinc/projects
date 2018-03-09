package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.report.*;
import com.rameses.rcp.framework.*;
import java.text.*;


public class BatchBillingModel extends CrudFormModel {
    
   @Service("WaterworksComputationService")
   def compSvc;
    
   @Service("WaterworksBatchBillingService")
   def batchSvc;
    
    
   def selectedItem;
   def getQuery() {
        return [batchid: entity.objid];
   } 
    
   def updateHandler = [
        beforeColumnUpdate: { item, colName, value ->
            if(colName.matches("reading|volume")) return false;
            try {
                def r = [:];
                if(colName == "reading") {
                    def p = [:];
                    p.volume = value - item.prevreading;
                    if(p.volume<0) throw new Exception("Current reading must be greater than prev reading");
                    p.objid = item.acctid; 
                    r.volume = p.volume;
                    r.amount = compSvc.compute(p);
                }
                else if(colName == "volume") {
                    def p = [:];
                    p.objid = item.acctid; 
                    r.volume = value;
                    r.amount = compSvc.compute(p);
                }

                def m = [_schemaname: 'waterworks_billing'];
                m[(colName)] = value;
                m.putAll( r );
                m.findBy = [objid: item.objid ];
                persistenceService.update( m );
                item.volume = m.volume;
                item.amount = m.amount;
                return true;
            }
            catch(e) {
                MsgBox.err(e);
                return false;
            }
        }
    ];
    
   public void post() {
       if( !MsgBox.confirm("You are about to finalize this transaction for billing. Proceed?")) return;
       batchSvc.post( [objid: entity.objid ]);
   } 
    
   def _printer;
   def getPrinter() {
       if(!_printer) {
           _printer = new TextPrinter();
           def res = ClientContext.currentContext.classLoader.getResourceAsStream( "com/rameses/gov/etracs/waterworks/reports/billing/waterbilling.rtxt");
           _printer.setTemplate( res );
       }
       return _printer;
   }
    
   def df = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
   public void printBill() {
       def mz = [_schemaname: 'waterworks_billing'];
       mz.findBy = [batchid: entity.objid];
       mz._limit = 10;
       def list = queryService.getList(mz);
       list.each {
           def p = [:];
            p.putAll( it );
            if(p.prevreadingdate ) p.prevreadingdate = df.format( p.prevreadingdate );
            if(p.readingdate ) p.readingdate = df.format( p.readingdate );
            p.discrate = 0.0;
            p.grandtotal = 0;
            p.subtotal = 0;
            p.penalty = 0;
            p.duedate = "";
            p.penalty = 0;
            p.classification = p.classification?.objid.toString().padRight(3," ")[0..2]
            getPrinter().print([ o: p ]);
       }
       
        /*
        def m = [:];
        m.name = "test".padLeft(10);
        m.address = "1111".padLeft(10);
        m.acctno = "22222".padLeft(10);
        m.indexno = "33333".padLeft(10);
        m.classification = "4444".padLeft(10);
        m.readingdate = "5555".padLeft(10);
        m.reading = "6666".padLeft(10);
        m.prevreadingdate = "7777".padLeft(10);
        m.prevreading = "88888".padLeft(10);
        m.diffpreprevreading = "9999".padLeft(10);
        m.amount = "1111111".padLeft(10);
        m.month = "222222".padLeft(10);
        m.discrete = "333333".padLeft(10);
        m.reducebill = "444444".padLeft(10);
        m.penalty = "555555".padLeft(10);
        m.totalbill = "66666".padLeft(10);
        m.duedate = "77777".padLeft(10);
        m.totalbillarrears = "88888".padLeft(10);
        getPrinter().print( m );
        */
   } 
    
}