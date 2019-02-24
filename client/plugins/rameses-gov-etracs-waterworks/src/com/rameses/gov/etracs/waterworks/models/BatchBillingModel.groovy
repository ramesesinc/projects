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
   
   @Service("WaterworksBeginBalanceService")
   def beginBalanceSvc;

   @Service("PersistenceService")
   def persistenceSvc;
    
   def consumptionUtil = ManagedObjects.instance.create(ConsumptionUtil.class);
   def billDispatcher = ManagedObjects.instance.create(BillDispatcherReceipt.class);
   
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
            o.batchid = entity.objid; 
            return batchSvc.getForBillingList( o );
        },
        processItem: { o->
            o.year = entity.schedule.year;
            o.month = entity.schedule.month;
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
        def p = [acctid: item.acctid, year: entity.schedule?.year, month: entity.schedule?.month]; 
        Modal.show("waterworks_consumption_history", p );
    }
    
    def viewBillReceipt = { item-> 
        if ( item ) {
            Modal.show("waterworks_batch_billing_menu_viewbillreceipt", [item: item, caller: this]);
        }
        return null; 
    }
    
    def rebill = { item->
        batchSvc.processBilling( [ 
            objid:item.objid,acctid:item.acctid,consumptionid:item.consumptionid, 
            year: entity.schedule.year, month: entity.schedule.month, meterstate:item.meterstate 
        ] );
        billHandler.reload();
    }
    
    def editBillFunc = { item-> 
        if ( item ) { 
            def p = [item: item, caller: this];
            p.beforeSave = { u-> p.u = u; } 
            p.handler = {
                if ( !p.u ) return; 
                item.surcharge = p.u.surcharge;
                item.interest = p.u.interest;
                item.otherfees = p.u.otherfees;
                item.arrears = p.u.arrears;
                item.credits = p.u.credits;
                billHandler.refreshSelectedItem(); 
            }
            Modal.show("waterworks_batch_billing_menu:edit", p); 
        } 
        return null; 
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
    
    def calcConsumption( item ) {
        if ( item.meterid ) {
            def p = [_schemaname: 'waterworks_meter'];
            p.findBy = [ objid: item.meterid]; 
            p.select = 'lastreading'; 
            item.prevreading = queryService.findFirst( p )?.lastreading;  
            if ( !item.prevreading ) item.prevreading = 0;
        }
        
        def o = [prevreading: item.prevreading, reading:item.reading];
        o.acctid = item.acctid;
        o.meterid = item.meterid;
        o.consumptionid = item.consumptionid;
        o.meterstate = item.meterstate;
        o.volume = item.volume;
        return compSvc.compute( o );
    }
   
    def recomputeConsumption = {item-> 
        def r = calcConsumption( item ); 
        if ( r ) item.putAll( r ); 
        readingHandler.refresh();
        return null; 
    } 
    
    def resetConsumption = {item-> 
        def p = [_schemaname: 'waterworks_account'];
        p.findBy = [ objid: item.acctid ]; 
        p.select = 'objid,meterid'; 
        def acct = queryService.findFirst( p ); 
        
        p = [_schemaname: 'waterworks_consumption'];
        p.findBy = [ objid: item.consumptionid ]; 
        p.meterid = acct?.meterid; 
        p.amount = p.amtpaid = 0.0; 
        p.volume = 0;
        
        def mparam = [_schemaname: 'waterworks_meter'];
        mparam.findBy = [ objid: p.meterid ]; 
        mparam.select = 'objid,lastreading'; 
        def meterinfo = queryService.findFirst( mparam ); 
        p.prevreading = p.reading = meterinfo?.lastreading; 
        persistenceSvc.update( p ); 
        
        item.reading = p.reading;
        item.prevreading = p.prevreading; 
        item.amount = item.amtpaid = 0.0;
        item.volume = p.volume; 
        readingHandler.reload(); 
        return null; 
    } 
    
    def beginBalance = { item->
        def h = [:];
        h.handler = { o->
            def p = [:];
            p.putAll( o ); 
            p.billid = item.objid;
            p.acctid = item.acctid; 
            p.meterid = item.meterid;
            p.meterstate = item.meterstate; 
            p.year = o.year;
            p.month = o.month; 
            p.scheduleid = entity.schedule.scheduleid;
            p.consumptionid = item.consumptionid;
            beginBalanceSvc.save( p );
            readingHandler.reload();
            billHandler.reload();
        };
        h.fields = [];
        h.fields << [name:'year', caption:'Year', type:'integer'];
        h.fields << [name:'month', caption:'Month', type:'monthlist'];
        h.fields << [name:'amount', caption:'Begin Balance', type:'decimal'];
        h.fields << [name:'reading', caption:'Last Reading', type:'integer'];
        Modal.show("dynamic:form", h, [title:"Begin Balance"] );
    };
    
    def getBillHandlerList() {
        def mnuList = [];
        mnuList << [value: 'View Account', func: viewAccount];
        if ( task?.state.toString().matches('for-review|for-reading')) {
            mnuList << [value: 'Recompute Bill', func:rebill];
            mnuList << [value: 'Begin Balance', func: beginBalance];
        } 
        mnuList << [value: 'View Bill', func: viewBilling];
        
        if ( task?.state.toString().matches('for-reading|for-approval|approved')) {
            mnuList << [value: 'Edit', func: editBillFunc];
        }
        return  mnuList; 
    }
   
    def getReadingHandlerList( def item ) {
        def mnuList = []; 
        def meterid = item.meterid; 
        if( task?.state.toString().matches("for-reading")) { 
            if( item.hold == 0 ) mnuList << [value: 'Hold', func:hold]; 
            if( item.hold == 1 ) mnuList << [value: 'Activate', func:hold]; 
            mnuList << [value: 'Recompute', func: recomputeConsumption]; 
            mnuList << [value: 'Reset', func: resetConsumption]; 
        }
        mnuList << [value: 'View Account', func:viewAccount]; 
        mnuList << [value: 'View Consumption History', func: viewConsumptionHistory]; 
        
        if ( task?.state == "approved" ) { 
            boolean allowed = ( 
                item.hold.toString() != '1' &&  
                item.acctstate.toString() == 'ACTIVE' &&  
                item.meterstate.toString() != 'DISCONNECTED'
            ); 
            if ( allowed ) { 
                mnuList << [value: 'View Bill Receipt', func: viewBillReceipt]; 
            }
        } 
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
            if(task?.state != "for-reading") return false;
            if (colName == "reading") {
                return true; 
            }
            else if (colName == "volume" && item.meterstate == 'DEFECTIVE') {
                return true; 
            }
            else {
                return false; 
            }
        },
        onColumnUpdate: { item,colName->
            if(colName.matches("reading|volume")) {
                def res = calcConsumption(item);
                item.putAll(res);
            }
        },
        isForceUpdate: {
            return true; 
        }
    ] ;
    
   def getPrinterList() {
       return printerService.getPrinters(); 
   }
   
   def cancelPrint = false;
   public void printBill() {
       def onprint = { printerName, text -> 
           if ( text ) {
               printerService.printString(printerName, text.toString() );
           } 
       }
       
       printBillImpl( null, onprint ); 
   }
   
    public void printBillImpl( optionMap, onPrintHandler ) {
        boolean pass = false;
        def printerName = null;
        def startbillno = null;
        def startseqno = null;
        def acctno = optionMap?.acctno; 
        def previewOnly = (optionMap?.previewOnly ? true : false); 
        if ( previewOnly ) {
            startbillno = 1; 
        }
        else {
            def h = [data: [acctno: optionMap?.acctno]];
            h.handler = { v->
                if ( v.billno == null ) throw new Exception("Start Bill No. is required"); 
                if ( v.billno <= 0 ) throw new Exception("Start Bill No. must be greater than zero"); 

                printerName = v.printername; 
                startbillno = v.billno;
                startseqno = v.seqno;
                acctno = v.acctno;
                pass = true; 
            } 
            h.fields = [];
            h.fields << [name:"printername", caption:'Select Printer', type:'combo', required:true, itemsObject:getPrinterList() ];
            h.fields << [name:"billno", caption:'Start Bill No.', type:'integer', required:true ];
            h.fields << [name:"seqno", caption:'Start Seq No.', type:'integer', enabled:(acctno ? false : true)];
            h.fields << [name:"acctno", caption:'Account No.', type:'text', enabled:(acctno ? false : true)];
            Modal.show("dynamic:form", h, [title: 'Start Bill Printing'] );
            if ( !pass ) return; 
        }
       
        def counter = 0; 
        def parm = [ 
            batchid: entity.objid, refbillno: startbillno, 
            startseqno: startseqno, acctno: acctno, previewOnly: previewOnly
        ]; 
        while ( true ) { 
            if ( cancelPrint ) break;

            def res = printSvc.process( parm );
            def list = res.list; 
            if ( !list ) break; 
            if ( cancelPrint ) break; 

            if ( onPrintHandler ) { 
                list.each { 
                    onPrintHandler( printerName, it ); 
                } 
            } 

            counter++; 
            if ( previewOnly ) {
                break; 
            }
            else if ( acctno ) {
                break;
            }
            
            waitPrintProc(); 
            parm.refbillno = res.refbillno; 
            parm.printed_list = res.printed_list; 
        } 

        if ( cancelPrint ) { 
            MsgBox.alert("printing has been cancelled");
        } else if ( counter == 0 ) { 
            MsgBox.alert("No available records that matches the criteria for printing. Please verify");
        } else if ( !previewOnly ) { 
            MsgBox.alert("printing finished");
        } 
    } 
    
   void printBillDispatcherReceipt() {
       billDispatcher.buildReport([ batchid: entity.objid ]); 
   } 
   
    def showMenuActions( inv ) { 
        def selItem = null; 
        def menus = null; 
        def invtype = inv.properties.type;
        if ( invtype == 'readingMenu' ) {
            if ( hasCallerProperty('selectedItem')) {
                selItem = caller.selectedItem; 
                if ( hasCallerMethod('getReadingHandlerList')) { 
                    menus = caller.getReadingHandlerList( selItem ); 
                }
            }

        } else if ( invtype == 'billingMenu' ) {
            if ( hasCallerProperty('selectedBillItem')) { 
                selItem = caller.selectedBillItem; 
                if ( hasCallerMethod('getBillHandlerList')) { 
                    menus = caller.getBillHandlerList(); 
                }
           }
        } 
       
        if ( !menus ) return null; 
       
        def ops = new PopupMenuOpener(); 
        menus.each{ ops.add( createActionMenu( it, selItem )); }
        return ops;
    }
   
    def createActionMenu( final menu, final data ) {
        return [
            getCaption: {
                return menu?.value.toString(); 
            }, 
            execute: { 
                if ( menu?.func ) { 
                    menu.func( data ); 
                } 
                return null; 
            } 
        ] as com.rameses.rcp.common.Action; 
    }
   
   
    private void waitPrintProc() {
        try { 
            new java.util.concurrent.LinkedBlockingQueue().poll(1, java.util.concurrent.TimeUnit.SECONDS);
        } catch (Throwable t) {
        }
    }
}