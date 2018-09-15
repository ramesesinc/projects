package gov.lgu.legazpi.rptis.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ModifyAnnotationModel {
    @Caller 
    def caller;
    
    @Service('PersistenceService')
    def persistence;
    
    @Service('LogService')
    def logSvc;
    
    def entity;
    def receipt;
    
    void init() {
        receipt = [:];
    }
    
    def update() {
        if (MsgBox.confirm('Update receipt information?')) {
            entity.putAll(receipt);
            entity._schemaname = 'faasannotation';
            persistence.update(entity);
            logSvc.log('modify-receipt', 'faasannotation', entity.objid);
            caller?.binding?.refresh();
            return '_close';
        }
        return null;
    }
}