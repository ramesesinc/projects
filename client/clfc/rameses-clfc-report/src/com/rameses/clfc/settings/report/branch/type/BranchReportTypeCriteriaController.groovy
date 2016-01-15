package com.rameses.clfc.settings.report.branch.type

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BranchReportTypeCriteriaController 
{
    @Binding
    def binding;
    def handler, entity, mode = 'read';
    
    void init() {
        entity = [:];
    }
    
    def criteria;
    def criteriaLookup = Inv.lookupOpener('branchreportcriteria:lookup',[
         onselect: { o->
            if (!entity) entity = [:];
            entity.criteria = o;
         }
    ]);
    
    def doOk() {
        if (handler) handler(entity);
        return '_close';
    }
    
    def doCancel() {
        return '_close';
    }
}

