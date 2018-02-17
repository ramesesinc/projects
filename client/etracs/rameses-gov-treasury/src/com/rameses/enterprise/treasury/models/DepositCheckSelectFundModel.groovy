package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class DepositCheckSelectFundModel  {

    def list;
    def selectedItem;
    def handler;

    def getFundList() {
       if(!selectedItem) return [];
       return selectedItem.funds;
    }
    
    def listHandler = [
        fetchList: { o->
            return list;
        }
    ] as EditorListModel;
    
    def doCancel() {
        return "_close";
    }

    def doOk() {
        if( list.find{ !it.fund } ) {
            throw new Exception("Please select fund for all items");
        }
        def h = list.collect { [objid: it.objid, depositfundid: it.fund.depositfundid, fundid: it.fund.objid  ]  };
        handler( h );
        return "_close";
    }
    
}    