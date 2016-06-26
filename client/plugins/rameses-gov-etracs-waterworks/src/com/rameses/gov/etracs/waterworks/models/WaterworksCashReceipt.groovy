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
     def payOption = "Full";  
    
     def monthList;
    
     public void init() {
        boolean pass = false;
        def params = [:];
        params.onselect = { o->
            super.init();
            loadData( o.acctno );
            pass = true;
        }
        Modal.show( Inv.lookupOpener( "cashreceipt:waterworks:lookup", params ) );
        if(!pass) throw new BreakException();
    }
    
    def itemListModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as BasicListModel;
            
    
    def loadData(acctno) {
        def info = cashReceiptSvc.getBilling([ refno: acctno ]);
        entity.putAll( info );
        monthList = entity.items.findAll{ it.year && it.month }.collect{ [year:it.year,month:it.month,monthname:it.monthname] }.unique().sort{(it.year * 12)+it.month};        
    }
    
    def loadBarcode() { 
        loadData( barcodeid );
        super.init(); 
        return "default";
    }      

    def showPayOption() {
        def h = { o->
            def op = [refno: entity.acctno];    
            if( o.month ) {
                op.month = o.month;
                payOption = "By Month until " + op.month.monthname + " " + op.month.year;
            }
            else {
                payOption = "Full";
            }
            def info = cashReceiptSvc.getBilling(op);
            entity.items = info.items;
            entity.amount = info.amount;
            super.updateBalances();
            itemListModel.reload();
        };    
        return Inv.lookupOpener("cashreceipt:waterworks:payoption", [handler:h, monthList: monthList]); 
    }
    
}