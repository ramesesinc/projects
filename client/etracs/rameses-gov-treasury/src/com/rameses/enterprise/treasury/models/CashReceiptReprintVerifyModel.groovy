package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;

class CashReceiptReprintVerifyModel  {
    
    @Service("CashReceiptReprintService")
    def service;

    @Script("User")
    def user;
    
    def username;
    def password;
    def remarks;
    
    def entity = [:];
    def receipt;
    boolean applySecurity = true;
    def handler;
    
    def doOk() {
        if(!receipt) throw new Exception("Please pass a receipt");
        entity.username = username;
        entity.password = user.encodePwd( password, username );
        entity.applysecurity = applySecurity;
        entity.receiptid = receipt.objid;
        entity.reason = remarks;
        service.verifyReprint( entity );
        handler();
        return "_close";
    } 

    def doCancel() {
        return "_close";
    }

    
}    