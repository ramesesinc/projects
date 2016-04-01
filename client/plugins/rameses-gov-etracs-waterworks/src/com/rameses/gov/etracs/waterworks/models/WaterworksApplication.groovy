package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksApplication extends WorkflowTaskModel {
    
    def feeList = [
        fetchList: {o->
            def m = [:];
            m._schemaname = "waterworks_account_ledger";
            m.findBy = [parentid: entity.account.objid];
            m.orderBy = "duedate";
            def list = getQueryService().getList(m);
            entity.total = list.sum{ it.balance };
            binding.refresh("entity.total");
            return list;
        }
    ] as BasicListModel;
    
    
    
}