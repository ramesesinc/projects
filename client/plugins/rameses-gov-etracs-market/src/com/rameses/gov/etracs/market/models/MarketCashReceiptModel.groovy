package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.models.*;
import com.rameses.util.*;
import com.rameses.functions.*;

public class MarketCashReceiptModel extends AbstractSimpleCashReceiptModel {
    
     @Service("MarketCashReceiptService")
     def cashReceiptSvc;
     
     @Service("MarketBillingService")
     def billingSvc;
    
     @Service('DateService') 
     def dateSvc; 
        
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Market Rental";
     def billdate;
     def acctFilter;
     def selectedItem;
     
     
     def df = new java.text.SimpleDateFormat("yyyy-MM-dd")
    
     public String getContextName() {
        return "market"; 
     }   
    
     public def getPaymentInfo(def o) {
         return null;
     }
     
     void init() {
         //do nothing
        billdate = dateSvc.getBasicServerDate();
        def s = { o->
            if( o ) {
                 acctFilter = o*.objid;
            }
            return null;
        };
        Modal.show( "market_collection_txntype:lookup" , [onselect:s] );
        if(!acctFilter) {
            throw new Exception("Please choose at least one type");
        }
        entity.billitems = [];
        entity.items = [];
     }
     
     void processBillingItem( def itm ) {
         def p = [:];
         p.putAll( itm );
         if( acctFilter ) p.filters = acctFilter;
         p.acctid = p.objid;
         def mm = billingSvc.getBilling( p );
         itm.putAll(mm);
     }   
    
     void updateReceipt() {
        entity.amount = 0;
        entity.items.clear();
        
        if( entity.billitems ) {
            if(entity.items == null )entity.items = [];
            
            entity.billitems.each { b->
                entity.items.addAll( b.items );
            };
            /*
            def grp = tlist.groupBy{ it.item };
            grp.each { k,v->
                def mv = [ item: k, amount: NumberUtil.round( v.sum{ it.amount } )  ];
                mv.remarks = v.findAll{ it.remarks }*.remarks?.join(";");
                entity.items << mv; 
            }
            */
            entity.amount = entity.items.sum{ it.amount };
        }
        updateBalances();
        binding.refresh("entity.amount");
     }     
    
     def itemHandler = [
         fetchList: {
             return entity.billitems;
         },
         onRemoveItem: { o->
             if( MsgBox.confirm("You are about to remove this item. Proceed")) {
                entity.billitems.remove(o);
                updateReceipt();
                return true;
             }
             return false;
         },
         onColumnUpdate: {i,n->
             if(n=="todate") {
                i.todate = df.parse( i.todate );
                i.remove("partial");    //remove partial if any...
                processBillingItem(i);
                updateReceipt();
             }
             else if( n=="amount") {
                def m = [objid:i.objid, partial: i.amount, todate: i.todate, fromdate: i.fromdate];
                processBillingItem( m );
                i.putAll(m);
                updateReceipt();
                itemHandler.reload();   
             }
         }
     ] as EditorListModel;  
     
     def getMarketAccountLookup() {
         def h = { o->
            if( entity.billitems.find{ it.objid == o.objid } )
                throw new Exception("Item already added.");
            def itm = [unitno: o.unitno, objid: o.objid, acctname: o.acctname ];
            itm.fromdate = o.dtstarted;
            if( o.lastdatepaid !=null ) {
                itm.fromdate = DateFunc.getDayAdd(o.lastdatepaid,1);
            }
            itm.todate = billdate;
            if( itm.todate.before(itm.fromdate) ) {
                boolean pass = false;
                def h = { k->
                    itm.todate = k;
                    pass = true;
                }
                Modal.show( "date:prompt", [handler: h ] );
                if( !pass ) throw new BreakException();
            }
            //MsgBox.alert("fromdate:" + itm.fromdate + "todate: " + itm.todate )
            processBillingItem(itm);
            entity.billitems << itm;
            updateReceipt();
            itemHandler.reload();
         }
         def pp = [onselect:h];
         if( entity.payer?.objid ) pp.ownerid = entity.payer.objid;
        return Inv.lookupOpener( "market_account:lookup", pp ); 
     }
     
     void clearAll() {
         if( entity.billitems == null ) entity.billitems = [];
         if( entity.items ) entity.items.clear();
         entity.billitems.clear();
         updateReceipt();
         binding.refresh();
     }
     
     def viewDetails() {
         if(!selectedItem ) throw new Exception("Please select an item");
         return Inv.lookupOpener("market:billitem:details", [entity:selectedItem] );
     }
     
     def viewCashReceipt() {
         if(!entity.items)
            throw new Exception("Please select at least one item")
         return Inv.lookupOpener( "cashreceipt:preview", [entity:entity]);
     }
     
     
     def applyPartial() {
         if(!selectedItem) throw new Exception("Please select an item");
         def h = { o->
            def m = [objid:selectedItem.objid, partial: o, todate: selectedItem.todate, fromdate: selectedItem.fromdate];
            processBillingItem( m );
            selectedItem.putAll(m);
            itemHandler.reload();
            updateReceipt();
         }
         return Inv.lookupOpener( "decimal:prompt", [handler:h, title:'Enter Partial amount'])
     }
    
   

}

