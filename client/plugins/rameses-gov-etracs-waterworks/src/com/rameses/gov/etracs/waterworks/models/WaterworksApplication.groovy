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
            def  m = [:];
            m._schemaname = 'waterworks_application_fee';
            m.findBy = [parentid: entity.objid];
            entity.fees = getQueryService().getList(m);
            entity.total = entity.fees.sum{ it.balance };
            binding.refresh("entity.total");
            return entity.fees;
        }
    ] as BasicListModel;
    
    def requirementList = [
        fetchList: {o->
            def  m = [:];
            m._schemaname = 'waterworks_application_requirement';
            m.findBy = [parentid: entity.objid];
            entity.requirements = getQueryService().getList(m);
            return entity.requirements;
        }
    ] as EditorListModel;
 
    void refresh() {
        binding.refresh();
    }
}