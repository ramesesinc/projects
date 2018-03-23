package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.report.*;
import com.rameses.rcp.framework.*;
import java.text.*;


public class BatchBillingModel extends WorkflowTaskModel {
    
   @Service("WaterworksComputationService")
   def compSvc;
    
   @Service("WaterworksBatchBillingService")
   def batchSvc;
    
   @Service("WaterworksScheduleService")
   def scheduleSvc;
   
   @Script("ReportService")
   def reportSvc;
    
   int year;
   int month;
   
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
           return "Zone " + entity.zone?.code + " " + entity.year + "-" + entity.month.toString().padLeft(2, "0") + " (" + task.title + ")";
       }
   } 
    
   @PropertyChangeListener
   def listener = [
       "entity.zone" : { o->
            year = 0;
            month = 0;
            if( o.year ) {
                int xx = ((o.year * 12)+o.month) + 1;
                entity.year = (int)(xx / 12);
                entity.month = (xx % 12);
            }
            else {
                entity.year = 0;
                entity.month = 0;
            }
       }
   ];
         
    
   void beforeSave(def mode) {
        if(mode == "create") {
             if(!entity.year) entity.year = year;
             if(!entity.month) entity.month = month;
             def m = [scheduleid: entity.zone.schedule.objid, year: entity.year, month: entity.month ];
             try {
                 def sked = scheduleSvc.getSchedule(m);
                 entity.putAll(sked);
                 entity.schedule = entity.zone.schedule;
             }
             catch( e) {
                 throw e;
             }
        }
   } 
    
   void afterSave() {
       open(); 
   }
   
   public def open() {
        def v = super.open();
        if( task.state == 'processing' ) {
            stat = batchSvc.getBilledStatus([ objid: entity.objid ]);
        }
        return v;
    }
    
    def stat;
    
    def progress = [
        getTotalCount : {
            if(stat == null ) throw new Exception("getTotalCount error. stat must not be null");
            return stat.totalcount;
        },
        fetchList: { o->
            def p = [ _schemaname: 'waterworks_billing' ];
            p.putAll( o );
            p.select = 'objid,acctid,billed,account.meter.*';
            p.findBy = [ batchid: entity.objid ];
            return queryService.getList( p );
        },
        processItem: { o->
            batchSvc.processBilling( o );
            binding.refresh('progressLabel');
        },
        onFinished: {
            binding.refresh();
        }
    ] as BatchProcessingModel;
    
   public boolean beforeSignal( def param  ) {
       if( task.state == 'processing' ) {
           //check stat balance befopre submitting process
            stat = batchSvc.getBilledStatus([ objid: entity.objid ]); 
            if( stat.balance > 0 ) throw new Exception("Cannot submit. Please complete process first");
       }
       return true;
   } 
    
   public void afterSignal(def transition, def task) {
       if( task.state == 'processing') {
           stat = batchSvc.getBilledStatus([ objid: entity.objid ]); 
       }
       billHandler.reload();
       readingHandler.reload();
   }
    
    def billHandler = [
        getContextMenu: { item, name-> 
            def mnuList = [];
            mnuList << [value: 'View Account', id:'view_account'];
            mnuList << [value: 'View Bill', id:'view_billing'];
            mnuList << [value: 'Recompute Bill', id:'recompute_billing']
            return  mnuList; 
	}, 
	callContextMenu: { item, menuitem-> 
            if( menuitem.id == "view_account") {
                Modal.show("waterworks_account:open", [entity: item.account ]);
            }
            else {
                throw new Exception("Menu not registered")
            }
	}    
    ];

    void updateVolumeAmount( def objid, def m ) {
        def p = [_schemaname: 'waterworks_billing'];
        p.findBy = [objid: objid ];
        p.putAll( m );
        persistenceService.update( p );
    }
    
    def readingHandler = [
        isColumnEditable: {item, colName -> 
            if( task.state != "for-reading") return false;
            if( item.account.meter?.objid == null ) return false;
            if( colName == "reading" ) {
                return true;
            }
            else if( colName =="volume") {
                //volume is only editable if meter is defective
                if(item.account?.meter.state == "DEFECTIVE") {
                    return true;
                }
                return false; 
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
                    if( value >= item.account.meter.capacity ) {
                        throw new Exception("Reading must be less than meter capacity");
                    }
                    if( value < item.prevreading ) {
                        value = value + item.account.meter.capacity;
                    }
                    def p = [:];
                    p.volume = value - item.prevreading;
                    p.objid = item.acctid; 
                    r.volume = p.volume;
                    def res = compSvc.compute(p);
                    r.amount = res.amount
                }
                else if(colName == "volume") {
                    def p = [:];
                    p.objid = item.acctid; 
                    p.volume = value;
                    def res = compSvc.compute(p);
                    r.volume = value;
                    r.amount = res.amount;
                }
                def m = [:];
                m[(colName)] = value;
                updateVolumeAmount( item.objid, m )
                item.volume = r.volume;
                item.amount = r.amount;
                return true;
            }
            catch(e) {
                MsgBox.err(e);
                return false;
            }
        },
        getContextMenu: { item, name-> 
            def mnuList = [];
            if ( item.account?.meter?.objid != null ) 
                mnuList << [value: 'View Meter', id:'view_meter'];
            mnuList << [value: 'Recompute', id:'recompute'];
            mnuList << [value: 'View Account', id:'view_account'];
            mnuList << [value: 'View Consumption History', id:'view_consumption_hist'];
            return  mnuList; 
	}, 
	callContextMenu: { item, menuitem-> 
            if( menuitem.id == "view_meter") {
                Modal.show("waterworks_meter:open", [entity: item.account.meter ] );
                readingHandler.reload();
            }
            else if( menuitem.id == "view_account") {
                Modal.show("waterworks_account:open", [entity: item.account ]);
                readingHandler.reload();
            }
            else if(menuitem.id == "recompute" ) {
                batchSvc.processBilling( item );
                readingHandler.reload();
            }
            else {
                throw new Exception("Menu not registered")
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
    
   //BACKUP CODES ----->
   /*
   void viewSchedule() {
        def m = [scheduleid: o.schedule.objid, year: year, month: month ];
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
   */    
  
    /*
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
    */
    
    
    
    
}