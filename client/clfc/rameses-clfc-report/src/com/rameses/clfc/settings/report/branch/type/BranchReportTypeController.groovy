package com.rameses.clfc.settings.report.branch.type

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class BranchReportTypeController extends CRUDController
{
    @Service("BranchReportCriteriaService")
    def criteriaSvc;
    
    String serviceName = 'BranchReportTypeService';
    String entityName = 'branchreportype';
        
    Map createPermission = [domain: 'ADMIN', role: 'ADMIN_SUPPORT'];
    Map editPermission = [domain: 'ADMIN', role: 'ADMIN_SUPPORT'];
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    
    void afterCreate( data ) {
        criteriaHandler?.reload();
    }
    
    def selectedCriteria;
    def criteriaHandler = [
        fetchList: { o->
            if (!entity.criteria) entity.criteria = [];
            return entity.criteria;
        },
        onOpenItem: { itm, colName->
            return Inv.lookupOpener('branchreporttypecriteria:open', [entity: itm, mode: mode]);
        }
    ] as BasicListModel;
    
    def addCriteria() {
        def handler = { o->
            if (!entity.criteria) entity.criteria = [];
            def i = entity.criteria.find{ it.criteria.code == o.criteria.code }
            if (i) throw new Exception(o.criteria.name + " has already been selected.");
            
            entity.criteria.add(o);
            criteriaHandler?.reload();
        }
        return Inv.lookupOpener('branchreporttypecriteria:create', [handler: handler, mode: mode]);
    }
    
    void removeCriteria() {
        if (!MsgBox.confirm("You are about to remove this criteria. Continue?")) return;
        
        entity.criteria.remove(selectedCriteria);
        
        criteriaHandler?.reload();
    }
    
    def prevcriteria;
    void afterEdit( data ) {
        prevcriteria = [];
        def item;
        data.criteria.each{ o->
            item = [:];
            item.putAll(o);
            prevcriteria.add(item);
        }
    }
    
    void afterCancel() {
        if (prevcriteria) {
            entity.criteria = [];
            entity.criteria.addAll(prevcriteria);
            criteriaHandler?.reload();
        }
        
    }
}

