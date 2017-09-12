package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class DepositModel extends CrudFormModel { 

    def fund;
    def fundList;
    
    def listHandler = [
        fetchList: { o->
            if( !fund ) return null;
            def m = [_schemaname:'liquidation_fund'];
            m.findBy = ["fund.objid": fund.objid ];
            m.where = ["depositid IS NULL"];
            m.orderBy = "liquidation.dtposted DESC"
            queryService.getList(m);
        },
        
        isMultiSelect : {
            return true;
        }
        
    ] as EditorListModel;
    
    public def initNew() {
        def m = [_schemaname:'fund',_limit:1000]
        fundList = queryService.getList(m);
        mode = "initial"
        return "init";
    }
    
    
} 