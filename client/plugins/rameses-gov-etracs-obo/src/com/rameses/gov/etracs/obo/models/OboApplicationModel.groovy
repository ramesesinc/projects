package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;

class OboApplicationModel extends WorkflowTaskModel {
    
    def ruleExecutor;
    
    InvokerFilter sectionFilter = { inv->
        if( !inv.properties?.section ) return false;
        if( !entity.permits ) return false;
        return entity.permits.find{ it.type.equalsIgnoreCase(inv.properties.section) }!=null;
    } as InvokerFilter;
    
    String getFormName() {
        return getSchemaName() + ":form";
    }
    
    String getBarcodeFieldname() {
        return "appno";
    }
    
    public String getTitle() {
        return entity.appno + " - " + task?.title;
    }
    
    public String getWindowTitle() {
        return entity.appno;
    }
    
    public String getFormId() {
        return entity.objid;
    }
    
    public def addAuxiliaryPermit() {
        def h = { o->
            if(entity.permits == null) entity.permits = [];
            entity.permits << o;
            //you must call this because sections are cached
            binding.refresh();
            listModel.reload();
        }
        return Inv.lookupOpener("obo_auxiliary_permit:create", [handler: h, app:entity] );
    }
    
    def selectedItem;
    def listModel = [
        fetchList: { o->
            return entity.permits;
        },
        openItem: { o,col->
            def op = Inv.lookupOpener( "obo_auxiliary_permit_"+o.type+":open", [entity: o]);
            op.target = 'popup';
            return op;
        }
    ] as BasicListModel;
    
    void removeItem() {
        if(!selectedItem) return;
        persistenceService.removeEntity( [_schemaname: 'obo_auxiliary_permit', objid: selectedItem.objid ]);
        entity.permits.remove(selectedItem);
        binding.refresh();
        listModel.reload();
    }
    
    
    void addAttachment()  {
        def p = [:];
        p.appid = entity.objid;
        p._schemaname = 'obo_application_attachment';
        
        def h = { o->
            attachmentListModel.reload();
        }
        Modal.show( "obo_application_attachment:create", [info:p, handler: h] );
    }

    def attachmentListModel = [
        fetchList: { o->
            def m = [_schemaname: 'obo_application_attachment'];
            m.findBy = [appid: entity.objid];
            return queryService.getList( m );
        },
        onOpenItem: { o,colName->
            def opener = Inv.lookupOpener("sys_file:open", [entity: [objid: o.fileid]] );
            opener.target = 'popup'; 
            return opener; 
        }
    ] as BasicListModel;
    
    void afterOpen() {
        entity.infos = [];
    }
    
}