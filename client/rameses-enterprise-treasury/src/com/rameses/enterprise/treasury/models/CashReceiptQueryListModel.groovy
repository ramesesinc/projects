package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class CashReceiptQueryListModel extends DefaultListController {
  
    @Service("CashReceiptVoidService")
    def voidSvc;
  
    private def tag; 
    
    boolean isAllowRevertVoid() { 
        if ( !selectedEntity ) return false; 
        if ( selectedEntity.remitted==true || selectedEntity.remitted==1 ) return false; 
        if ( selectedEntity.voided==true || selectedEntity.voided==1 ) return true; 
        
        return false; 
    } 
    
    def revertVoid() {
        def params = [ receipt: selectedEntity ]; 
        params.handler = { o-> 
            selectedEntity.voided = 0;
            binding?.refresh();
        } 
        return InvokerUtil.lookupOpener( "cashreceiptvoid:revert", params );  
    }   
    
    def sortType;
    def sortTypes = ['ASC','DESC']; 
    
    def sortField; 
    def sortFields = [
        [name: 'receiptno', caption: "Receipt No."], 
        [name: 'receiptdate', caption: "Receipt Date"],
        [name: 'paidby', caption: "Payer"] 
    ];  
    
    protected void beforeFetchList(Map params) {
        if ( sortField?.name ) { 
            params.sortfield = sortField.name; 
            params.sorttype = sortType; 
        } 
        
        if ( tag.toString().toUpperCase() == 'COLLECTOR') {
            params.collectorid = com.rameses.rcp.framework.ClientContext.currentContext.headers.USERID; 
        }
    }
    
    void init( def inv ) { 
        tag = inv.properties.tag; 
    } 
}