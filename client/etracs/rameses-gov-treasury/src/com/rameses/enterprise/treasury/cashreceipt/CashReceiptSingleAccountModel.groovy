package com.rameses.enterprise.treasury.cashreceipt; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
        
public class CashReceiptSingleAccountModel extends BasicCashReceipt {
    
    void init() {
        super.init();
        loadCollectionTypeAccount(); 
        afterInit(); 
    }

    public void afterInit() {} 
    
    public String getEntityType() {
        return 'individual';
    } 
    
    void loadCollectionTypeAccount() {
        def m = [_schemaname:'collectiontype_account']; 
        m.findBy = [collectiontypeid: entity.collectiontype?.objid ]; 
        
        entity.items = [];
        queryService.getList( m ).each{ 
            it.objid = 'CRI-'+ new java.rmi.server.UID(); 
            it.receiptid = entity.objid;  
            it.item = it.account; 
            it.amount = it.defaultvalue; 
            entity.items << it; 
        } 
        
        if ( !entity.items ) 
            throw new Exception('Collection Type Account must be provided first.'); 
            
        updateBalances(); 
    }     
}