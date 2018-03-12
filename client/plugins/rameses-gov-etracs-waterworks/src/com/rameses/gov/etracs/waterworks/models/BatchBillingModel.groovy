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
   def selectedBillItem;
    
   def getQuery() {
        return [batchid: entity.objid];
   } 
    
   public String getTitle() {
       if(mode == "create" ) {
           return "New Batch Billing";
       }
       else {
           return "Zone " + entity.zone?.code + " " + entity.year + "-" + entity.month.toString().padLeft(2, "0") + " (" + entity.state + ")";
       }
   } 
    
   @PropertyChangeListener
   def listener = [
       "entity.zone" : { o->
            def m = [scheduleid: o.schedule.objid, year: entity.year, month: entity.month ];
            try {
                def sked = scheduleSvc.getSchedule(m);
                entity.putAll(sked);
                entity.schedule = o.schedule;
                binding.refresh();
            }
            catch( e) {
                MsgBox.err( e );
            }
       }
   ];
    
   public void afterCreate() {
       open();
   } 

   public def open() {
        def p = super.open();
        if( entity.state == "PROCESSING") {
            stat = batchSvc.getBilledStatus([ objid: entity.objid ]);
            return "processing";
        }
        else {
            return "default";
        }
    }
    
    private def progdata = [:];
    def stat;
    def progressHandler = [
        initHandler : { o-> 
            o.value = 0; 
            o.maxvalue = stat.totalcount;
            progdata = o; 
        },
        stopHandler : { 
            progdata.cancelled = true; 
        },
        startHandler : {
            progdata.cancelled = false;
            startProcess(); 
        }
    ];
    
    private void startProcess() { 
       progdata._start = 0; 
       progdata._limit = 10;
       progdata._success = false; 
       
       def p = [ _schemaname: 'waterworks_billing' ];
       p.select = 'objid,acctid,billed';
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
               
               def item = list[i];
               if ( item.billed.toString() == '1' ) continue; 
               batchSvc.processBilling( item );
           }
       }
       if ( progdata.cancelled == true ) return;
       progdata._success = true;         
       progdata.finish(); 
   }

    void changeState( String state ) {
       def m = [_schemaname:'waterworks_billing_batch'];
       m.findBy = [objid: entity.objid];
       m.state = state;
       persistenceService.update( m );
       entity.state = state;
    }
    
    def submitForProcessing() {
       changeState( "PROCESSING" );
       stat = batchSvc.getBilledStatus([ objid: entity.objid ]);
       return "processing";
    } 
    
    def submitForReview() {
       stat = batchSvc.getBilledStatus([ objid: entity.objid ]); 
       if( stat.balance > 0 ) throw new Exception("Cannot submit. Please complete process first");
       if( !MsgBox.confirm("You are about to submit this for review. Proceed?")) return
       changeState( 'FOR_REVIEW' );
       return "default";
    } 
    
    def revertForProcessing() {
       def m = [_schemaname:'waterworks_billing'];
       m.findBy = [batchid: entity.objid];
       m.billed = 0;
       persistenceService.update( m );
       return submitForProcessing();
    }
    
    void submitForReading() {
       if( !MsgBox.confirm("You are about to submit this for reading. Proceed?")) return
       changeState( 'FOR_READING' );
    } 
    
    void submitForApproval() {
        if( !MsgBox.confirm("You are about to submit this for approval. Proceed?")) return
       changeState( 'FOR_APPROVAL' );
    } 
    
    void approve() {
        if( !MsgBox.confirm("You are about to approve this batch. Proceed?")) return
       changeState( 'APPROVED' );
    } 
    
    void post() {
        if( !MsgBox.confirm("You are about to post this batch. Proceed?")) return
        batchSvc.post([ objid: entity.objid ]);
        entity.state = 'POSTED';
    } 
    
    def updateHandler = [
        isColumnEditable: {item, colName -> 
            if( colName == "reading" ) {
                return (entity.state == "FOR_READING");
            }
            else if( colName =="volume") {
                return (entity.state == "FOR_READING");
            }
            else {
                return false;
            }
        },
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
       mz.orderBy = 'account.stuboutnode.indexno'; 
       def list = queryService.getList(mz);
       list.each {
           def p = [:];
            p.putAll( entity ); 
            p.putAll( it );
            p.penalty = it.surcharge + it.interest;
            p.grandtotal = it.amount + p.penalty;
            p.classification = p.account.classification?.objid.toString().padRight(3," ")[0..2];
            p.blockseqno = (p.zone?.code +'-'+ p.account.stuboutnode?.indexno);
            reportSvc.print( "waterworks_billing" , [ o: p ] );
       }
   } 
    
    
}