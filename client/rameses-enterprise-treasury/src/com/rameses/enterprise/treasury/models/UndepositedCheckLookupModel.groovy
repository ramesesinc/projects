package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class UndepositedCheckLookupModel  { 

    @Service("CollectionDepositService")
    def depositSvc;

    @Binding
    def binding;
    
    def refids;    
    def items;
    def onselect;
    def selections;
    def amount = 0;
    def excludeids;
    
    def checkModel = [
        fetchList: {
            if(!refids) return [];
            items = depositSvc.getUndepositedChecks([refids: refids, excludeids: excludeids]);
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