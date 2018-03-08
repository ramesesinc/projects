package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class FundModel extends CrudFormModel {

   void afterCreate() {
        entity.groupid = caller.selectedNode.objid;
        entity.system = 0;
        entity.state = 'DRAFT';
    }
    
    def selectedFund;
    
    def subFundListHandler = [
        fetchList: { o->
            def m = [_schemaname:'fund'];
            m.findBy = [parentid: entity.objid ];
            m.orderBy = "code";
            return queryService.getList( m );
        }
    ] as BasicListModel;
    
    def showSubFundForm(def handler, def data ) {
       def fields = [
           [name:'code', caption:'Code', required:true],
           [name:'title', caption:'Title', required:true] 
       ];
       if(!data) data = [:];
       def op = Inv.lookupOpener("dynamic:form", [ data: data, fields: fields, handler: handler, formTitle: 'Enter Sub Fund' ] );
       op.caption = "Sub Fund";
       return op;
    }
    
    def addSubFund() {
       def h = { o ->
           persistenceService.create(o);
           subFundListHandler.reload();
       }
       def m = [_schemaname : "fund" ];
       m.groupid = caller.selectedNode.objid;
       m.system = 0;
       m.state = entity.state;
       m.parentid = entity.objid;
       return showSubFundForm( h, m );
    }
    
    def editSubFund() {
        if(!selectedFund) throw new Exception("Select a fund first");
        def h = { o->
            def m = [_schemaname: 'fund'];
            m.findBy = [objid: selectedFund.objid];
            m.code = o.code;
            m.title = o.title;
            persistenceService.update(m);
            subFundListHandler.reload();
        }
        def d = [code:selectedFund.code, title: selectedFund.title];
        return showSubFundForm( h, d );
    }
    
    void removeSubFund() {
        if(!selectedFund) throw new Exception("Select a fund first");
        def m = [_schemaname: 'fund'];
        m.findBy = [objid: selectedFund.objid];
        persistenceService.removeEntity( m );
        subFundListHandler.reload();
    }
    
}