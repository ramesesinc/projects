package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class CashReceiptVoidModel  {

    @Binding
    def binding;

    @Caller
    def caller;

    @Script("User")
    def user;
    
    @Service("CashReceiptVoidService")
    def service;
    
    boolean allowCreate = false;
    boolean allowEdit = false;

    def username;
    def password;
    def remarks;
    
    def entity;
    def receipt;
    def handler;

    void create( invo ) { 
        if (receipt.voided) {
            throw new Exception("This transaction is already voided") 
        }     
        if ( invo?.properties?.tag != 'admin' ) { 
            validatePermission(); 
        } 

        entity = [objid: "CRVOID"+ new java.rmi.server.UID() ];
        entity.receipt = receipt;
    }

    void open( invo ) { 
        receipt = caller.entity;
        if ( receipt.voided ) {
            throw new Exception("This transaction is already voided") 
        } 
        if ( invo?.properties?.tag != 'admin' ) { 
            validatePermission(); 
        } 

        entity = [objid: "CRVOID"+ new java.rmi.server.UID() ];
        entity.receipt = receipt;
    }

    def doOk() {
        if( MsgBox.confirm( "You are about to void this receipt. Please ensure you have the original receipt on hand. Continue?")) {
            entity.username = username;
            entity.password = user.encodePwd( password, username );
            entity.reason = remarks;
            service.post( entity );
            receipt.voided = true;
            if ( handler ) { 
                handler(receipt);
            } else if ( caller ) {
                try { 
                    caller.refresh(); 
                } catch(Throwable t) {
                    t.printStackTrace(); 
                } 
            } 
            return "_close";
        } 
    } 

    def doCancel() {
        return "_close";
    }

    private void validatePermission() {
        def options = service.getOptions();  
        if ( options?.collector_allow_void_cashreceipt == 'false' ) { 
            throw new Exception('You are not allowed to void this transaction'); 
        } 
    }             
    
    boolean isAllowVoid() { 
        if ( entity.remitted==true || entity.remitted==1 ) return false; 
        if ( entity.voided==true || entity.voided==1 ) return false; 
        
        return true; 
    } 
  
    
}    