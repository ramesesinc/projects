package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
                
public class WaterworksCapturePayment {

    @Caller 
    def caller; 
    
    @Binding 
    def binding; 
    
    @Service('QueryService')
    def querySvc; 
    
    @Service("WaterworksCashReceiptService")
    def cashReceiptSvc;
    
    @Service("WaterworksPaymentService") 
    def paymentSvc; 
        
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    
    def billdata;
    def miscdata; 
    def otherdata;
    def handler; 
    def entity; 
    def year; 
    
    
    @PropertyChangeListener
    def listener = [
        "entity.billingcycle" : { o-> 
            loadConsumption(); 
            loadBillItems( o );
        }
    ];
    
    void init() { 
        entity = [ objid: 'CPAY' + new java.rmi.server.UID() ]; 
        billdata = [:];
        miscdata = [:];
        otherdata = [:]; 
    }
    
    def getMasterEntity() {
        return caller?.getMasterEntity();   
    } 
        
    def getMonthList() { 
        if( !year ) return [];
        def m = [_schemaname:'waterworks_billing_cycle'];
        m.findBy = [sectorid: masterEntity.sectorid, year:year];
        return querySvc.getList( m );
    }   

    def billListModel = [
        fetchList: { o-> 
            if ( billdata.items == null ) {
                billdata.items = []; 
            } 
            return billdata.items;
        }, 
        onRemoveItem: { o->
            if ( !MsgBox.confirm("You are about to remove this entry. Continue?")) return false;
            
            billdata.items.remove( o ); 
            billdata.amount = billdata.items.sum{( it.amount? it.amount: 0.0 )} 
            if ( billdata.amount == null ) billdata.amount = 0.0; 
            
            binding.refresh('amountgroup'); 
            return true;
        } 
    ] as EditorListModel;    
    
    void loadBillItems( o ) { 
        if ( billdata.items == null ) billdata.items = []; 
        else billdata.items.clear(); 
        
        def m = [ refno: masterEntity.acctno ]; 
        if ( entity.billingcycle ) { 
            m.month = [ 
                year: entity.billingcycle.year, 
                month: entity.billingcycle.month, 
                monthname: entity.billingcycle.monthname 
            ];
            def info = cashReceiptSvc.getBilling( m ); 
            billdata.items = info.items; 
            billdata.amount = info.amount; 
        } else { 
            billdata.amount = 0.0; 
        } 
        binding.refresh('amountgroup'); 
        billListModel.reload(); 
    }     
    
    
    def miscListModel = [
        fetchList: { o-> 
            if ( miscdata.items == null ) {
                miscdata.items = []; 
            }
            return miscdata.items;
        }, 
        onAddItem: {o-> 
            o.objid = 'CPAYI' + new java.rmi.server.UID();
            miscdata.items << o; 
            binding.refresh('amountgroup'); 
        },
        onColumnUpdate: {o,name-> 
            binding.refresh('amountgroup'); 
        },
        onRemoveItem: { o->
            if ( !MsgBox.confirm("You are about to remove this entry. Continue?")) return false;
            
            miscdata.items.remove( o );
            binding.refresh('amountgroup'); 
            return true;
        } 
    ] as EditorListModel;
    
    def selectedMiscItem;
    def getLookupItems() { 
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup", [ 
            "query.txntype" : "cashreceipt",  
            onselect:{ o-> 
                selectedMiscItem.item = o; 
                selectedMiscItem.amount = o.defaultvalue; 
            } 
        ]); 
    } 
    
    def numFormatter = new java.text.DecimalFormat("#,##0.00"); 
    def getTotalAmount() {
        def miscamount = miscdata.items?.sum{( it.amount? it.amount: 0.0 )} 
        entity.totalamount = (miscamount? miscamount: 0.0) + (billdata.amount? billdata.amount: 0.0);
        return entity.totalamount; 
    } 
    def getBillAmtFormatted() { 
        def value = billdata.amount; 
        if ( value == null ) value = 0.0; 
        return numFormatter.format( value ); 
    }
    def getMiscAmtFormatted() { 
        def value = miscdata.items?.sum{( it.amount? it.amount: 0.0 )} 
        if ( value == null ) value = 0.0; 
        return numFormatter.format( value ); 
    } 
    def getTotalAmtFormatted() { 
        def value = getTotalAmount(); 
        if ( value == null ) value = 0.0; 
        return numFormatter.format( value ); 
    }         
    
    def cancel() { 
        return '_close'; 
    } 
    def save() { 
        if ( getTotalAmount() != entity.amount ) 
            throw new Exception('Receipt Amount must be equal to the Total Amount'); 
        
        if ( MsgBox.confirm('You are about to post this payment. Continue?')) { 
            if ( entity.items == null ) entity.items = []; 
            else entity.items.clear(); 
            
            if ( billdata.items ) entity.items.addAll( billdata.items ); 
            if ( miscdata.items ) entity.items.addAll( miscdata.items ); 
            
            entity.acctid = masterEntity.objid; 
            entity.reftype = 'cashreceiptcapture'; 
            entity.txnmode = 'CAPTURE'; 
            entity.voided = 0; 
            entity.items.each{ 
                it.parentid = entity.objid; 
                it.acctid = entity.acctid;
                it.txnrefid = it.refid;
            } 
            paymentSvc.postCreate( entity ); 
            MsgBox.alert( 'Transaction successfully posted' ); 
            if ( handler ) {
                handler( entity ); 
            } else if ( caller ) { 
                try { 
                    caller.reload();  
                } catch(Throwable t) { 
                    t.printStackTrace(); 
                } 
            }
            return '_close'; 
        } 
        return null; 
    }
    
    void loadConsumption() { 
        otherdata.consumption = null;         
        if ( entity.billingcycle ) { 
            def m = [_schemaname: 'waterworks_consumption', select: 'objid,volume']; 
            m.findBy = [ acctid: masterEntity.objid, billingcycleid: entity.billingcycle.objid ]; 
            otherdata.consumption = querySvc.findFirst( m ); 
        }
    }
} 