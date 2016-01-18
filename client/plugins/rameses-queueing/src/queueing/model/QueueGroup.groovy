package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;


class QueueGroup {

    @Service("QueueMgmtService")
    def service;
    
    String title = "Queue Group";
    def entity;
    
    def mode;
    
    
    void create() {
        entity = [:];
        if( entity.sections == null ) entity.sections = [];
        mode = "create";
    }
    
    void open() {
        entity = service.open([objid: entity.objid]);
        mode = "read";
    }
    
    def selectedSection;
    
    def sectionListModel = [
        fetchList: { o->
            return entity.sections;
        },
        onAddItem: {o->
            o.sortorder = entity.sections.size()+1;
            entity.sections << o;
        },
        onRemoveItem: { o->
            entity.sections.remove(o); 
            //reorder sort
            int i = 0;
            entity.sections.each {
                it.sortorder = i++;
            }
        }
    ] as EditorListModel;
    
    void edit() {
        mode = "edit";
    }
    
    void save() {
        if( !MsgBox.confirm('Are you sure you want to save this record?') ) return ;
        if(mode == 'create')  {
            service.create(entity);
        }
        else {
            service.update(entity);
        }
        mode = "read";
    }
    
}