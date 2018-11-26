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
    
   @Service("WaterworksBatchBillPrintingService")
   def printSvc;
   
    
   def consumptionUtil = ManagedObjects.instance.create(ConsumptionUtil.class);
    
   def selectedItem;
   def selectedBillItem;
   
   boolean hasDate = false; 
   def stat;
     
   def printerService = new PrinterService();
   
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
        return "Zone " + entity.zone?.code + " " + entity.schedule.year + "-" + String.format('%02d', entity.schedule.month) + " (" + task.title + ")";
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
            return batchSvc.getForBillingList([batchid:entity.objid])
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
        consumptionUtil.compute(z, null);
        readingHandler.reload();
    }
   
    def viewMeter = { o->
        Modal.show("waterworks_meter:open", [entity:item.meter] );
        readingHandler.reload();
        billHandler.reload();
    }
    
    def viewAccount = { o->
        Modal.show("waterworks_account:open", [entity: [objid:o.acctid] ]); 
        readingHandler.reload();
        billHandler.reload();
   }

    def viewBilling = { item->
        Modal.show("waterworks_account_billing", ['query.objid': item.acctid ]);
    }
    
    def viewConsumptionHistory = { item->
        Modal.show("waterworks_consumption_history", [item: item] );
    }
    
    def rebill = { item->
        batchSvc.processBilling( [objid:item.objid,acctid:item.acctid,consumptionid:item.consumptionid] );
        billHandler.reload();
    }
    
    def hold = { item->
        def i = item.hold ? 0 : 1;
        def h = [:];
        h.data = [hold: i, objid: item.consumptionid ];
        h.fields = [];
        h._schemaname = "waterworks_consumption";
        h._log_schemaname = "waterworks_changelog";
        h._bypass_check_diff = true;
        //h.beforeSave = { o-> }
        h.handler = {item.hold = i;}
        Modal.show("changeinfo", h, [title: "Change Hold Status"]);
    }
   
    def recomputeConsumption = {item->
        def o = [prevreading: item.prevreading, reading:item.reading];
        o.acctid = item.acctid;
        o.meterid = item.meterid;
        o.consumptionid = item.consumptionid;
        o.meterstate = item.meterstate;
        o.volume = item.volume;
        return compSvc.compute( z );
    } 
    
    def getBillHandlerList() {
        def mnuList = [];
        mnuList << [value: 'View Account', func: viewAccount];
        if(task?.state.matches('for-review|for-reading') ) {
            mnuList << [value: 'Recompute Bill', func:rebill]
        } 
        mnuList << [value: 'View Bill', func: viewBilling];
        return  mnuList; 
    }
   
    def getReadingHandlerList(def item) {
        def meterid = item.meterid; 
        def mnuList = [];
        if( task?.state == "for-reading" ) {
            if(item.hold == 0 ) mnuList << [value: 'Edit Reading/Volume', func:editConsumption];
            if(item.hold == 0 ) mnuList << [value: 'Hold', func:hold];
            if(item.hold == 1 ) mnuList << [value: 'Activate', func:hold];
        }
        mnuList << [value: 'View Account', func:viewAccount];
        mnuList << [value: 'View Consumption History', func: viewConsumptionHistory];
        mnuList << [value: 'Recompute', func: recomputeConsumption];
        return  mnuList; 
    }
   
   
    def billHandler = [
        getContextMenu: { item, name-> 
            return getBillHandlerList();
	}, 
	callContextMenu: { item, menuitem-> 
            menuitem.func( item );
	}    
    ];

    def readingHandler = [
        getContextMenu: { item, name-> 
            return getReadingHandlerList(item);
	}, 
	callContextMenu: { item, menuitem-> 
           menuitem.func( item );
	},
        isColumnEditable: {item,colName->
            if(colName == "reading") {
                return (item.meterstate=="ACTIVE"); 
            }
        },
        onColumnUpdate: { item,colName->
            if(colName=="reading") {
                def res = recomputeConsumption(item);
                item.putAll(res);
            }
        }
    ];
    
   def cancelPrint = false;
   public void printBill() {
       def printerList = printerService.getPrinters();
       /*
       if(printerList.size()==0)
            throw new Exception("There are no printer names registered in printer list");
       def printerName = printerList.iterator().next();
       */
        def printerName = "epson";
       
       def startno = null;
       def h = [:];
       h.handler = { v->
           startno = v.billno;
       };
       h.fields = [];
       h.fields << [name:"billno", caption:'Enter Start Bill No', type:'integer', required:true ];
       Modal.show("dynamic:form", h, [title: 'Start Bill Printing'] );
       if(!startno) return;
       
       def parm = [refbillno: startno, batchid : entity.objid ]
       while(true) {
           def res = printSvc.process( parm );
           def list = res.list;
           if(!list) break;
           if(cancelPrint) break;
           list.each {
               printerService.printString(printerName, it.toString() );
           }
           parm.refbillno = res.refbillno;
       }
       MsgBox.alert("printing finished");
   } 
    
}