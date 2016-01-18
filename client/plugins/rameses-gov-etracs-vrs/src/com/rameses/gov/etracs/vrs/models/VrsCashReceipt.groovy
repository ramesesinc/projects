package com.rameses.gov.etracs.vrs.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class VrsCashReceipt extends BasicCashReceipt {
    
    @Service("VrsBillingService")
    def billSvc;
    
     //we specify this so print detail will appear.
     String entityName = "misc_cashreceipt";
     String title = "Vehicle Registration";
     def barcodeid;
    
     def payOption = [type:'FULL'];   
    
     void init() {
        super.init();
        boolean pass = false;
        def params = [:];
        params.onselect = { o->
            loadInfo( o );
            pass = true;
        }
        Modal.show( Inv.lookupOpener( "cashreceipt:vrs:lookup", params ) );
        if(!pass) throw new BreakException();
    }
            
    void loadBarcode() {
        loadInfo( [appno: barcodeid] );
    }   
    
    void loadInfo( def e ) {
        e.payOption = payOption; 
        def m = billSvc.getBilling( e ); 
        entity.payer = [objid: m.owner.objid, name: m.owner.name ];
        entity.paidby = m.owner.name + ' ['+m.controlid+']';
        entity.appno = m.appno;
        entity.applicationid = m.objid;
        entity.paidbyaddress = m.owner?.address?.text;
        entity.billitems = m.billitems;
        entity.amount = m.amount;
        entity.items = m.items;
    } 
    
    def showPayOption() {
        def h = { o ->
            payOption = o;
            loadInfo( [objid: entity.applicationid] );
            binding.refresh();
            itemListModel.reload();
        }
        return Inv.lookupOpener( "cashreceipt:payoption:vrs", [payOption: payOption, handler:h]);
    }
    
    def previewReceipt() {
        return Inv.lookupOpener( "cashreceipt:preview", [entity: entity] );
    }
    
    /*
    def showReceipt() {
        return Inv.lookupOpener("businessreceipt:view", [entity:entity]);
    }
            
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
   
    def itemListModel = [
        fetchList: { o->
            return entity.billitems;
        }
    ]as BasicListModel;
    
}