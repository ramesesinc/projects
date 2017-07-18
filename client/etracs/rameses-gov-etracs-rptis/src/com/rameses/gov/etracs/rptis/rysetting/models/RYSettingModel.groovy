package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.interfaces.*;
import com.rameses.gov.etracs.rptis.util.*;

public class RYSettingModel extends PageFlowController
{
    @Caller
    def caller 
    
    @Binding 
    def binding
    
    @Invoker
    def invoker;
    
    def MODE_READ   = 'read';
    def MODE_EDIT   = 'edit';
    def MODE_CREATE = 'create';
    def mode = MODE_READ;
    
    def entity;
    def tabOpeners;
    
    public String getEntityName(){
        return invoker.properties.entityName;
    }
    
    public def getService(){
        return ServiceLookup.create(invoker.properties.serviceName);
    }
    
    public String getSettingTitle(){
        return invoker.properties.caption;
    }
    
    def create(){
        entity = [objid:'LRY' + new java.rmi.server.UID()];
        mode = MODE_CREATE;
        return super.start("new");
    }    
    
    void createSetting(){
        entity.putAll(service.create(entity));
        caller?.search();
        mode = MODE_READ;
    }
        
    def open(){
        entity.putAll(service.open(entity));
        mode = MODE_READ;
        modeChanged();
        return super.start("main");
    }
    
    void edit(){
        mode = MODE_EDIT;
        modeChanged();
    }
    
    void saveCreate(){
        entity.putAll(service.create(entity));
        mode = MODE_READ;
        modeChanged();
    }
    
    void saveUpdate(){
        entity.putAll(service.update(entity));
        mode = MODE_READ;
        modeChanged();
    }
    
    void cancelEdit(){
        open();
        mode = MODE_READ;
    }
    
    void approve(){
        entity.putAll(service.approve(entity));
        mode = MODE_READ;
        modeChanged();
    }
    
    void delete(){
        service.removeEntity(entity);
        caller.search();
    }
    
    void modeChanged(){
        tabOpeners.each{
            SubPage info = (SubPage)it.handle;
            info.modeChanged(mode);
        }
    }
    
    def tabHandler = [
        getOpeners : {
            def type = 'rysetting:info';
            tabOpeners = InvokerUtil.lookupOpeners(type, [entity:entity, service:getService()]);
            
            try{
                type = getEntityName() + ':info';
                tabOpeners += InvokerUtil.lookupOpeners(type, [entity:entity, service:getService()]);
            }
            catch(e){
                println e.message;
            }
            def lastidx = 10000;
            tabOpeners.sort{a,b -> 
                if (a.properties.index == null) a.properties.index = lastidx;
                if (b.properties.index == null) b.properties.index = lastidx;
                a.properties.index <=> b.properties.index
            }
            return tabOpeners;
        }
    ] as TabbedPaneModel;
    
}
