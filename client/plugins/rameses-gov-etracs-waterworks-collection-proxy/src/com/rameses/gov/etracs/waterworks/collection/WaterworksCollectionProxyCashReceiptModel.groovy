package com.rameses.gov.etracs.waterworks.collection;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

public class WaterworksCollectionProxyCashReceiptModel extends com.rameses.enterprise.treasury.models.AbstractCashReceipt {
    
    @Service("WaterworksCollectionProxyService")
    def billingSvc;
    
    def prefix;

    def status;   
    def selectedItem;
    def txnid;
    
    //store something here in query that is constantly being sent to the server
    def query = [:];
    
    def billAmount = 0;
    
    def billItemList = [];
    
    boolean amountSpecified = false;
    
    public String getTitle() {
        if( invoker.properties.formTitle ) {
            return ExpressionResolver.getInstance().evalString(invoker.properties.formTitle,this);
        }
        if( invoker.caption ) {
            return invoker.caption;
        }
        return getContextName();
    }
    
    public boolean getAllowDeposit() {
        return true;
    }
    
    public String getContextName() {
        def pfn = invoker.properties.contextName;
        if(pfn) return pfn;
        pfn = workunit?.info?.workunit_properties?.contextName;
        if ( pfn ) return pfn; 
        return super.getSchemaName(); 
    }
    
    def _payOption = null;
    public String getPayOption() {
        if(_payOption == null ) {
            _payOption = invoker.properties.payOption;
            if(!_payOption ) _payOption = workunit?.info?.workunit_properties?.payOption;
        } 
        return _payOption; 
    }
    
    public String getRulename() {
        String s = invoker.properties.rulename;
        if( s!=null ) {
            return s;
        }
        s = workunit?.info?.workunit_properties?.rulename;
        if( s != null ) return s;
    }

    public String getDetails() {
        return "Details";
    }

    void afterLoadInfo() {;}
    boolean onNoItemsFound() { return false;}
    
    void lookupTxn() {
        def lookupName = getContextName() + ":cashreceipt_lookup"
        try {
            def h = { o->
                txnid = o.txnid;
                binding.refresh();
                return null;
            }
            def opener = Inv.lookupOpener(lookupName, [onselect: h ]);
            Modal.show( opener );
        }
        catch(ex) { 
            ex.printStackTrace(); 
            MsgBox.alert(lookupName + " not found");
        }
    }
    
    void findTxn() {
        if(txnid.contains(":")) txnid = txnid.split(":")[1];
        loadInfo([id:txnid, action:'open']);
    }
    
    def loadInfo(def p) {
        p.collectiontype = entity.collectiontype;
        p.billdate = entity.receiptdate;
        
        //must replace the action so it will reflect the action passed;
        query.action = p.action;
        p.putAll( query );
        def pp = [ rulename: getRulename(), params: p ]; 
        def info = null;
        try {
            info = billingSvc.getInfo( pp );
        }
        catch(serverErr) {
            if(p.action == "barcode") super.doClose();
            throw serverErr;
        }
        
        if( !info.billitems ) {
            if( getAllowDeposit() ) {
                if ( !MsgBox.confirm('There are no bill items found. Do you want to pay in advance?')) 
                    throw new BreakException(); 
                    
                def amt = MsgBox.prompt("Enter amount for advance payment. ");
                if(!amt) throw new BreakException();
                pp.params.amtpaid = new BigDecimal(""+amt);
                info = billingSvc.getInfo( pp );
            }
            else {
                throw new Exception("No bill items found");
            }
        }
        entity.putAll(info);
        billItemList = info.items;
        reloadItems(); 
        //afterLoadInfo();
        //loadPayOptions();
        return super.start("entry");
    }
    
    void reloadItems() {
        entity.items = [];
        entity.items.addAll( billItemList );
        itemListModel.reload();
        if(binding) binding.refresh();
        updateBalances();
    }
    
    public void updateBalances() {
        billAmount = NumberUtil.round( billItemList.sum{ it.amount } );
        super.updateBalances();
    }
    
    
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
    
    def showPayOptions() {
        if( amountSpecified ) 
            throw new Exception("Please reset amount specified first to Pay All");
        if( !getPayOption() ) return null;
        def m = [:];
        m.onselect = { o->
            loadInfo( [id: txnid, action:'payoption', payoption: o ] );
        }
        return Inv.lookupOpener( getPayOption(), m);
    }
    
    void specifyPayAmount() {
        def o = MsgBox.prompt("Enter Pay Amount");
        if(!o) return null;
        def p = [amtpaid: o, id:txnid, action:'open' ];
        loadInfo( p );
        amountSpecified = true;
    }
    
    void payAll() {
        def p = [id:txnid, action:'open' ];
        loadInfo( p );
        amountSpecified = false;
    }
    
    def resetPayOption() {
        loadInfo( [id: txnid] );
    }
    
    public void validateBeforePost() {
        if( entity.balancedue !=  0 )
            throw new Exception("Amount must be equal to amount paid");
    }
    
    def itemListModel = [
        fetchList: { o->
            return entity.billitems;
        }
    ] as BasicListModel;
          
    
}