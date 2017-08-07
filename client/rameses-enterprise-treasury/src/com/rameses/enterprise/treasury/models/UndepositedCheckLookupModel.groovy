package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class UndepositedCheckLookupModel  { 

    @Service("QueryService")
    def querySvc;

    @Binding
    def binding;
    
    def refids;    
    def items;
    def onselect;
    def selections;
    def amount = 0;
    
    def checkModel = [
        fetchList: {
            if(!refids) return [];
            def q = [_schemaname: 'liquidation_noncashpayment' ];
            q.where = [" liquidationid IN ( '" +  refids.join("','") +  "') "];
            items = querySvc.getList( q )*.check;
            return items;
        },
        isMultiSelect: { return true },
        afterSelectionChange: { 
            selections = checkModel.selectedValue;
            amount = selections.sum{it.amount};
            binding.refresh("amount");
        }
    ] as BasicListModel;

    def doOk() {
        if(!onselect) throw new Exception("Please specify an onselect handler");
        onselect( selections );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }

} 