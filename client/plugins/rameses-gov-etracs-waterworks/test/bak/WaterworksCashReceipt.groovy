package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class WaterworksCashReceiptPage extends BasicCashReceipt {
    
     @Service("WaterworksCashReceiptService")
     def cashReceiptSvc;
    
     def payOption = [type:'FULL']; 
    
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Waterworks";
     def barcodeid;
     
     void init() {
        super.init();
        boolean pass = false;
        def params = [:];
        params.onselect = { o->
            loadInfo( o );
            pass = true;
        }
        Modal.show( Inv.lookupOpener( "cashreceipt:waterworks:lookup", params ) );
        if(!pass) throw new BreakException();
    }
    
    def loadBarcode() { 
        def info = cashReceiptSvc.getBilling([ refno: barcodeid ]);
        entity = [formtype: "serial", formno:"51", txnmode: 'ONLINE'];
        entity.collectiontype = info.collectiontype;
        entity = service.init( entity );

        entity.payer = info.payer;
        entity.items = info.items;
        entity.amount = info.amount;
        entity.paidby = info.paidby;
        entity.paidbyaddress = info.paidbyaddress;
        entity.remarks = info.remarks;
        entity.info = info.info; 
        super.init(); 

        entity.billitems = info.billitems; 
        return 'default';
    }      

    
    /*void loadInfo( def e ) {
        e.payOption = payOption; 
        def m = billSvc.getBilling( e ); 
        entity.payer = [objid: m.owner.objid, name: m.owner.name ];
        entity.paidby = m.acctname + ' ['+m.acctno+']';
        entity.acctno = m.acctno;
        entity.acctid = m.objid;
        entity.acctname = m.acctname;
        entity.paidbyaddress = m.address.text;
        entity.billitems = m.billitems;
        entity.amount = m.amount;
        entity.items = m.items;
        monthList = m.remove("monthList");
    } 
    
    def showPayOption() {
        def h = { o ->
            payOption = o;
            loadInfo( [objid: entity.acctid] );
            binding.refresh();
            itemListModel.reload();
        }
        return Inv.lookupOpener( "cashreceipt:payoption:waterworks", [monthList: monthList, payOption: payOption, handler:h]);
    }*/
    
    /*
    void removeTaxcredits() {
        def taxcredits = entity.items.findAll{ it.type=='taxcredit' }
        if (taxcredits) entity.items.removeAll( taxcredits ); 
    } 
            
    void validateBeforePost() {}
            
    void beforeUpdateBalance() { 
        removeTaxcredits(); 
    } 
            
    void postError() {
        removeTaxcredits();
    } 
            
    void beforePost() { 
        removeTaxcredits(); 

        if(entity.totalcredit > 0) {
            if (excessAcct?.objid == null) 
                throw new Exception('Tax credit account is required. Please check ExcessPayment rule'); 

            entity.items << [item: excessAcct, amount: entity.totalcredit, remarks:'', type:'taxcredit', indexno:200];
        } 
    } 
    */
   
    def previewReceipt() {
        return Inv.lookupOpener( "cashreceipt:preview", [entity: entity] );
    }
    
    def billItemListModel = [
        fetchList: { o-> 
            return entity.billitems; 
        } 
    ] as BasicListModel;
    
}