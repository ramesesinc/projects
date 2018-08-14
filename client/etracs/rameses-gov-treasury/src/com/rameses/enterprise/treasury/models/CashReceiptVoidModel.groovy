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
    boolean applySecurity = false;

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
        if( !allowVoid ) throw new Exception("Void is not allowed"); 
        entity = [objid: "CRVOID"+ new java.rmi.server.UID() ];
        entity.receipt = receipt;
        applySecurity = false;
    }

    void open( invo ) { 
        receipt = caller.entity;
        if ( receipt.voided ) {
            throw new Exception("This transaction is already voided") 
        } 
        if( !allowVoid ) throw new Exception("Void is not allowed");
        entity = [objid: "CRVOID"+ new java.rmi.server.UID() ];
        entity.receipt = receipt;
        applySecurity = true;
    }

    def doOk() {
        if( MsgBox.confirm( "You are about to void this receipt. Please ensure you have the original receipt on hand. Continue?")) {
            if( applySecurity ) {
                entity.username = username;
                entity.password = user.encodePwd( password, username );
            }
            entity.applysecurity = applySecurity;
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
          
    
    boolean isAllowVoid() { 
        if ( entity.remitted==true || entity.remitted==1 ) return false; 
        if ( entity.voided==true || entity.voided==1 ) return false; 
        
        return true; 
    } 
  
    
}    