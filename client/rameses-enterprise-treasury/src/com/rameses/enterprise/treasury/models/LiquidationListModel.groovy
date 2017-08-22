package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.seti2.models.*;

public class LiquidationListModel extends CrudListModel {

    @Script("User")
    def user;

    @Service('LiquidationService') 
    def liquidationSvc; 

    @FormId
    public String getFormId() {
        return "liquidation-list-"+invoker.properties.tag;
    }

    def deposit() {
        return Inv.lookupOpener( "collectiondeposit:create" );
    } 
    
    void initForDeposit() {
        super.init();
        def n = nodeList.find{ it.id == 'approved' };
        if(n) selectedNode = n;
    }

} 