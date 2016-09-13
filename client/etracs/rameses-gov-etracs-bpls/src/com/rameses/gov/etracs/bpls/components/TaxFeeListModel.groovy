package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

class TaxFeeListModel extends ComponentBean {
        
    def selectedItem;

    public def getEntity() {
        return getValue();
    }
    
    void reload() {
        if(!entity.taxfees) entity.taxfees = [];
        if(!entity.totals) entity.totals = [:];
        listModel.reload();
    }

    def listModel = [
        fetchList: { o->
            return entity.taxfees;
        }
    ] as BasicListModel;
    
    void showDetails() {
        MsgBox.alert('show details ' + selectedItem );
    }
    
}