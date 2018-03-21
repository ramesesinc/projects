package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;

public class WaterworksCashReceipt extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt {
    
     @Service("WaterworksCashReceiptService")
     def cashReceiptSvc;
    
     def barcodeid;
     String entityName = "misc_cashreceipt"
     
     String title = "Waterworks";
     def payOption = "Full Payment";  
    
     def monthList;
     
     def billdata = [:]; 
     def miscdata = [:]; 
    
     public void init() { 
        def wsno = MsgBox.prompt('Enter Waterworks Account number:');  
        if ( !wsno ) throw new BreakException();  
                
        loadData( wsno ); 
    }
    
    def billListModel = [
        fetchList: { o-> 
            if ( billdata.items == null ) {
                billdata.items = []; 
            }            
            return billdata.items;
        }
    ] as BasicListModel;

    def miscListModel = [
        fetchList: { o-> 
            if ( miscdata.items == null ) {
                miscdata.items = []; 
            }
            return miscdata.items;
        }, 
        onAddItem: {o-> 
            o.objid = 'RCTI' + new java.rmi.server.UID();
            miscdata.items << o; 
            updateBalances();
        },
        onColumnUpdate: {o,name-> 
            updateBalances();
        },
        onRemoveItem: { o->
            if ( !MsgBox.confirm("You are about to remove this entry. Continue?")) return false;
            
            miscdata.items.remove( o );
            updateBalances();
            return true;
        }        
    ] as EditorListModel;
    
    def selectedMiscItem;
    def getLookupItems() { 
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup", [ 
            "query.txntype"       : "cashreceipt",
            "query.collectorid"   : entity.collector.objid,
            "query.fund"          : entity.collectiontype.fund,
            "query.collectiontype": entity.collectiontype, 
            onselect:{ o-> 
                selectedMiscItem.item = o; 
                selectedMiscItem.amount = o.defaultvalue; 
            } 
        ]); 
    } 

    
    def loadData(acctno) { 
        def info = cashReceiptSvc.getBilling([ refno: acctno, billdate: entity.receiptdate, txnmode: entity.txnmode ]);
        entity.putAll( info );
        billdata.items = entity.remove('items'); 
        billdata.amount = entity.amount; 
        monthList = billdata.items.findAll{ it.year && it.month }.collect{ [year:it.year,month:it.month,monthname:it.monthname] }.unique().sort{(it.year * 12)+it.month}; 
    }
    
    def loadBarcode() { 
        loadData( barcodeid );
        super.init(); 
        return "default";
    }      

    def showPayOption() {
        def h = { o->
            def op = [refno: entity.acctno, billdate: entity.receiptdate, txnmode: entity.txnmode];    
            if( o.month ) {
                op.month = o.month;
                payOption = "By Month until " + op.month.monthname + " " + op.month.year;
            }
            else {
                payOption = "Full Payment";
            }
            def info = cashReceiptSvc.getBilling(op);
            billdata.items = info.items;
            billdata.amount = info.amount;
            billListModel.reload();
            updateBalances();
        } 
        return Inv.lookupOpener("cashreceipt:waterworks:payoption", [handler:h, monthList: monthList]); 
    }
    
    public void updateBalances() { 
        miscdata.amount = miscdata.items.sum{( it.amount? it.amount : 0.0 )} 
        entity.amount = (miscdata.amount? miscdata.amount: 0.0) + (billdata.amount? billdata.amount: 0.0); 
        super.updateBalances(); 
        if ( binding ) binding.refresh('amountgroup'); 
    } 
    
    def post() { 
        if ( entity.items==null ) entity.items = []; 
        else entity.items.clear(); 
        
        if ( billdata.items ) entity.items.addAll( billdata.items ); 
        if ( miscdata.items ) entity.items.addAll( miscdata.items ); 
        
        return super.post(); 
    }
         
    def numFormatter = new java.text.DecimalFormat("#,##0.00"); 
    def getBillAmtFormatted() { 
        def value = billdata.amount; 
        if ( value == null ) value = 0.0; 
        return numFormatter.format( value ); 
    }
    def getMiscAmtFormatted() { 
        def value = miscdata.amount; 
        if ( value == null ) value = 0.0; 
        return numFormatter.format( value ); 
    }    
    def getTotalAmtFormatted() { 
        def value = getTotalAmount(); 
        if ( value == null ) value = 0.0; 
        return numFormatter.format( value ); 
    }     
}