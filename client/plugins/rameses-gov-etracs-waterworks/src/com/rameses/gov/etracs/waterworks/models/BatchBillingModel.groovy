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
    
   def consumptionUtil = ManagedObjects.instance.create(ConsumptionUtil.class);
    
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
            def p = [ _schemaname: 'vw_waterworks_billing' ];
            p.putAll( o );
            p.select = 'objid,acctid,meterid,consumptionid,meterstate';
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
    
    /*
    public boolean getRedflag( def item ) {
        return ( item.averageconsumption > 0 && (item.volume < (item.averageconsumption * 0.70) || item.volume > (item.averageconsumption * 1.30))); 
    }
    */
    
    public boolean beforeSignal( def param  ) {
       if( task.state == 'processing' ) {
           //check stat balance before submitting process
            stat = batchSvc.getBilledStatus([ objid: entity.objid ]); 
            if( stat.balance > 0 ) throw new Exception("Cannot submit. Please complete process first");
       }
       return true;
   } 
    
   //functions 
   def editConsumption =  { o->
        def z = [prevreading:o.prevreading, reading:o.reading, 
            volume:o.volume, consumptionid:o.consumptionid, acctid:o.acctid, 
            meterid:o.meterid, meterstate: o.meterstate];
        def h = { x->
            //o.putAll( x );
        }
        consumptionUtil.compute(z, h)
    }
   
    def viewMeter = { o->
        Modal.show("waterworks_meter:open", [entity:item.meter] );
        readingHandler.reload();
    }
    
    def viewAccount = { o->
        Modal.show("waterworks_account:open", [entity: [objid:o.acctid] ]); 
    }

    def viewBilling = { item->
        Modal.show("waterworks_account_billing", ['query.objid': item.acctid ]);
    }
    
    def viewConsumptionHistory = { item->
        Modal.show("waterworks_consumption_history", [item: item] );
    }
    
    def rebill = { item->
        batchSvc.processBilling( [objid:item.objid,acctid:item.acctid,consumptionid:item.consumptionid] );
        readingHandler.reload();
        billHandler.reload();
    }
   
    def changePrevReading = { item->
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
    }

    def changeVolume = { item->
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
    }

    def getBillHandlerList() {
        def mnuList = [];
        mnuList << [value: 'View Account', func: viewAccount];
        if(task?.state == 'for-review' ) {
            mnuList << [value: 'Recompute Bill', func:rebill]
        } 
        mnuList << [value: 'View Bill', func: viewBilling];
        return  mnuList; 
    }
   
    def getReadingHandlerList(def item) {
        def meterid = item.meterid; 
        def mnuList = [];
        if( task?.state == "for-reading" ) {
            mnuList << [value: 'Edit Consumption', func:editConsumption];
        }
        mnuList << [value: 'View Account', func:viewAccount];
        mnuList << [value: 'View Consumption History', func: viewConsumptionHistory];
        return  mnuList; 
    }
   
   
    def billHandler = [
        getContextMenu: { item, name-> 
            return getBillHandlerList();
	}, 
	callContextMenu: { item, menuitem-> 
            menuitem.func( item );
            billHandler.reload();
	}    
    ];

    def readingHandler = [
        getContextMenu: { item, name-> 
            return getReadingHandlerList(item);
	}, 
	callContextMenu: { item, menuitem-> 
           menuitem.func( item );
           readingHandler.reload();
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
    
}