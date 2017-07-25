package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;

public class MarketAccountModel extends CrudFormModel {
    
    @Service('MarketAccountService') 
    def svc; 
    
    void afterOpen() {
        itemHandler.reload();
        //unitListModel.reload();
    }
    
    def itemHandler = [
        fetchList: {
            return entity.recurringfees;
        }
    ] as BasicListModel;
    
    def unitListModel = [
        fetchList: {
            return entity.units;
        }
    ] as BasicListModel;
    
    def showEditMenu() {
        return showDropdownMenu("editActions");
    }
                    
    void closeAccount() {
        if( MsgBox.confirm("Close account will release the rented units and deactivate this account. Proceed?")) {
            def s = svc.closeAccount([ objid: entity.objid ]);
            entity.state = s.state; 
        }
    }
    void blockAccount() {
        if( MsgBox.confirm("You are about to block this account. Proceed?")) {
            def s = svc.blockAccount([ objid: entity.objid ]); 
            entity.state = s.state; 
        }    
    }
    void unblockAccount() {
        if( MsgBox.confirm("You are about to unblock this account. Proceed?")) {
            def s = svc.unblockAccount([ objid: entity.objid ]); 
            entity.state = s.state; 
        }    
    }
}