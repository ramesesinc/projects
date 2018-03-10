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
    
   @Service("WaterworksScheduleService")
   def scheduleSvc;
   
    @Script("ReportService")
   def reportSvc;
    
    
   def selectedItem;
   def getQuery() {
        return [batchid: entity.objid];
   } 
    
   @PropertyChangeListener
   def listener = [
       "entity.zone" : { o->
            entity.scheduleid = o.schedule.objid;
            def m = [scheduleid: entity.scheduleid, year: entity.year, month: entity.month ];
            try {
                def sked = scheduleSvc.getSchedule(m);
                entity.putAll(sked);
                binding.refresh();
            }
            catch( e) {
                MsgBox.err( e );
            }
       }
   ]
    
    
   def updateHandler = [
        beforeColumnUpdate: { item, colName, value ->
            if(!colName.matches("reading|volume")) return false;
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
            //getPrinter().print([ o: p ]);
            reportSvc.print( "waterworks_billing" , [ o: p ] );
            return;
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
    


   private def progdata = [:]; 
   
   boolean isCanSubmitForReading() {
       return (mode == 'read' && entity.state.toString().equalsIgnoreCase('DRAFT')); 
   }
   def submitForReading() { 
       def stat = batchSvc.getBilledStatus([ objid: entity.objid ]);  
       if ( stat.balance > 0 ) { 
           def params = [:]; 
           params.initHandler = { o-> 
               o.value = 0; 
               o.maxvalue = stat.totalcount;
               progdata = o; 
           }
           params.stopHandler = { 
               progdata.cancelled = true; 
           }
           params.startHandler = {
               progdata.cancelled = false;
               startProcess(); 
           }
           
           def op = Inv.lookupOpener('waterworks_reading_preparation', params); 
           op.target = 'self';
           return op; 
       }
       
       doSubmitForReading();
       return null; 
   } 
   private void doSubmitForReading() {
       if( MsgBox.confirm("You are about to submit this transaction for reading. Proceed?")) {
           def o = batchSvc.submitForReading([ objid: entity.objid ]); 
           if ( o?.state ) entity.state = o.state; 
       }
   }
   
   private void startProcess() { 
       progdata._start = 0; 
       progdata._limit = 10;
       progdata._success = false; 
       
       def p = [ _schemaname: 'waterworks_billing' ];
       p.select = 'objid,acctid,unpaidamt,billed';
       p.findBy = [ batchid: entity.objid ]; 
       p._limit = progdata._limit+1; 
       p._start = progdata._start; 
       
       def more_result = true; 
       while( more_result ) {
           if ( progdata.cancelled == true ) break;
           
           def startidx = p._start; 
           def list = qryService.getList( p ); 
           more_result = (list.size() > progdata._limit); 
           if ( more_result ) { 
               p._start += progdata._limit; 
           } 
           for (int i=0; i < list.size(); i++) {
               if ( progdata.cancelled == true ) {
                   more_result = false; 
                   break; 
               }
               
               startidx += 1; 
               progdata.value = startidx; 
               progdata.refresh();  
               processItem( list[i] ); 
           }
       }
       if ( progdata.cancelled == true ) return;
       progdata._success = true;         
       progdata.finish(); 
       doSubmitForReading(); 
   }
   
   private void processItem( item ) {
       if ( item.billed.toString() == '1' ) return; 
       
       // execute the billing service here...
       
       def p = [ _schemaname: 'waterworks_billing', billed: 1 ];
       p.findBy = [ objid: item.objid ];
       persistenceService.update( p ); 
   }
   
   public boolean isCanPost() {
       return (mode == 'read' && entity.state.toString().equalsIgnoreCase('FOR_READING')); 
   }
   public void post() { 
       if( !MsgBox.confirm("You are about to post this transaction for billing. Proceed?")) return;
       batchSvc.post([ objid: entity.objid ]); 
   } 

}