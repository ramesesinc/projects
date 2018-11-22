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
    
   @Service("WaterworksBatchBillProcessorService")
   def batchSvc;
    
   @Script("ReportService")
   def reportSvc;
    
   
   def selectedItem;
   def selectedBillItem;
   
   boolean hasDate = false; 
   def stat;
     
   /*
    *   This method is used in style rule condition and DataTable column expression 
    */
   def getRoot() { 
       return this; 
   }
   
   
   def getQuery() {
        return [batchid: entity.objid];
   } 
    
   public String getTitle() {
        return "Zone " + entity.zone?.code + " " + entity.year + "-" + String.format('%06d', entity.month) + " (" + task.title + ")";
   } 
    
   public def open() {
        def v = super.open();
        if( task.state == 'processing' ) {
            stat = batchSvc.getBilledStatus([ objid: entity.objid ]);
        }
        return v;
    }
    
    public void afterSignal(def transition, def task) {
       if( task.state == 'processing') {
           stat = batchSvc.getBilledStatus([ objid: entity.objid ]); 
       }
    }
    
    def progress = [
        getTotalCount : {
            if(stat == null ) throw new Exception("getTotalCount error. stat must not be null");
            return stat.totalcount;
        },
        fetchList: { o->
            def p = [ _schemaname: 'waterworks_billing' ];
            p.putAll( o );
            p.select = 'objid,acctid,consumptionid';
            p.findBy = [ batchid: entity.objid ];
            p.where = ["billed = 0"];
            p.orderBy = "billno";
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
    
    public boolean getRedflag( def item ) {
        return ( item.averageconsumption > 0 && (item.volume < (item.averageconsumption * 0.70) || item.volume > (item.averageconsumption * 1.30))); 
    }
    
    public boolean beforeSignal( def param  ) {
       if( task.state == 'processing' ) {
           //check stat balance before submitting process
            stat = batchSvc.getBilledStatus([ objid: entity.objid ]); 
            if( stat.balance > 0 ) throw new Exception("Cannot submit. Please complete process first");
       }
       return true;
   } 
    
   
    
   void updateVolumeAmount( def objid, def m ) {
        def p = [_schemaname: 'waterworks_consumption'];
        p.findBy = [objid: objid ];
        p.putAll( m );
        persistenceService.update( p );
    } 
    
   def actions = [
       "view_meter" : {item->  
            Modal.show("waterworks_meter:open", [entity: item.account.meter ] );
            readingHandler.reload();
        },
       "view_account": {item-> 
            Modal.show("waterworks_account:open", [entity: item.account ]); 
            readingHandler.reload();
        },
       "view_billing": {item-> 
            Modal.show("waterworks_account_billing", ['query.objid': item.acctid ]); 
        },
       "view_consumption_hist": {item-> 
             Modal.show("waterworks_consumption_history", [item: item] );
        },
        "rebill": { item->
            batchSvc.processBilling( item );
            readingHandler.reload();
            billHandler.reload();
        },
        "change_prev_reading" : { item ->
            def h = [:];
            h.fields = [
                [name:'prevreading', caption:'Enter Prev Reading', datatype:'integer'],
                [name:'prevreadingdate', caption:'Prev Reading Date', datatype:'date' ]
            ];
            h.data = [ prevreading: item.prevreading, prevreadingdate: item.prevreadingdate ];
            h.entity = item;
            h.reftype = "waterworks_consumption";
            h.refid = item.objid;
            Modal.show("waterworks_changeinfo", h, [title:"Enter Prev Reading"]);
            readingHandler.reload();
        },
        "change_volume": { item->
            def h = [:];
            h.fields = [
                [name:'volume', caption:'Enter Volume', datatype:'integer'],
                [name:'amount', caption:'Amount', datatype:'decimal', enabled:false, depends:"volume" ]
            ];
            h.data = [ volume: item.volume, amount: item.amount ];
            h.entity = item;
            h.listener = [ "volume" :  { ii, newValue -> ii.amount = newValue * 10; } ]
            h.reftype = "waterworks_consumption";
            h.refid = item.objid;
            Modal.show("waterworks_changeinfo", h, [title:"Enter Volume"]);
            readingHandler.reload();
        }
   ] 
    
   def billHandler = [
        getContextMenu: { item, name-> 
            def mnuList = [];
            mnuList << [value: 'View Account', id:'view_account'];
            if(task?.state == 'for-review' ) {
                mnuList << [value: 'Recompute Bill', id:'rebill']
            } 
            mnuList << [value: 'View Bill', id:'view_billing'];
            return  mnuList; 
	}, 
	callContextMenu: { item, menuitem-> 
            def act = actions[(menuitem.id)];
            act( item );
	}    
    ];

    def readingHandler = [
        isColumnEditable: {item, colName -> 
            if( colName != "reading" ) return false;
            if( task?.state != "for-reading") return false; 
            
            def meterid = item.account?.meterid; 
            if ( meterid == null ) meterid = item.account?.meter?.objid; 
            if( meterid == null ) return false; 
            return true;
        },
        beforeColumnUpdate: { item, colName, value ->
            if( colName != "reading") return false;
            try {
                if( value >= item.account.meter.capacity ) {
                    throw new Exception("Reading must be less than meter capacity");
                }
                if( value < item.prevreading ) {
                    value = value + item.account.meter.capacity;
                }
                def p = [:];
                p.volume = value - item.prevreading;
                p.objid = item.acctid; 
                
                def res = compSvc.compute(p);
                res.reading = value;
                if(!res.volume) res.volume = p.volume;
                updateVolumeAmount( item.objid, res );
                item.putAll( res );
                return true;
            }
            catch(e) {
                MsgBox.err(e);
                return false;
            }
        },
        getContextMenu: { item, name-> 
            def mnuList = [];
            def meterid = item.account?.meterid; 
            if ( meterid == null ) meterid = item.account?.meter?.objid;
            
            if ( meterid != null && task?.state == 'for-reading' ) {
                mnuList << [value: 'Change Prev Reading', id:'change_prev_reading'];
                mnuList << [value: 'View Meter', id:'view_meter'];
            } 
            if( task?.state == "for-reading" ) {
                mnuList << [value: 'Change Volume', id:'change_volume'];
            }
            if( task?.state == 'for-review') {
                mnuList << [value: 'Recompute', id:'rebill'];
            }
            mnuList << [value: 'View Account', id:'view_account'];
            mnuList << [value: 'View Consumption History', id:'view_consumption_hist'];
            return  mnuList; 
	}, 
	callContextMenu: { item, menuitem-> 
            def act = actions[(menuitem.id)];
            act( item );
	}
    ];
    
   def df = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
   public void printBill() {
       def mz = [_schemaname: 'waterworks_consumption'];
       mz.findBy = [batchid: entity.objid];
       mz.orderBy = 'account.stuboutnode.indexno'; 
       def list = queryService.getList(mz);
       list.each {
           def p = [:];
            p.putAll( entity ); 
            p.putAll( it );
            p.penalty = it.surcharge + it.interest;
            p.grandtotal = it.amount + p.penalty;
            p.classification = p.account.classification?.toString().padRight(3," ")[0..2];
            p.blockseqno = (p.zone?.code +'-'+ p.account.stuboutnode?.indexno);
            reportSvc.print( "waterworks_consumption" , [ o: p ] );
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
  
    
    
    
}