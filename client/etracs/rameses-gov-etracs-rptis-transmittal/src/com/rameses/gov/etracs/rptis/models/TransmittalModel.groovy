package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;


public abstract class TransmittalModel extends PageFlowController
{
    @Binding
    def binding;
    
    @Service('RPTTransmittalService')
    def svc 
    
    @Service('LGUService')
    def lguSvc;
            
    @Invoker
    def invoker 
    
    def MODE_CREATE = 'create';
    def MODE_EDIT   = 'edit';
    def MODE_READ   = 'read';
    
    def STATE_DRAFT         = 'DRAFT';
    def STATE_SUBMITTED     = 'SUBMITTED';
    def STATE_FORAPPROVAL   = 'FORAPPROVAL';
    def STATE_APPROVED      = 'APPROVED';
    
    def mode;
    def entity;
    def oncomplete;
    
    String entityName = 'rpttransmittal';
    
    public abstract String getFileType();
    
    public final def getService(){ return svc; }
    
    public String getTitle(){
        def t = getFileType().toUpperCase() + ' Transmittal (' + entity.state.toLowerCase() + ')';
        return t;
    }
    
    def init(){
        entity = [
            objid  : 'T' + new java.rmi.server.UID(),
            state  : STATE_DRAFT,
            filetype : getFileType(),
        ];
        entity.items = [];
        mode = MODE_CREATE;
        listHandler?.reload();
        return super.signal('init');
    }
    
    def open(){
        entity = svc.open(entity);
        entity.items = svc.getItems(entity);
        mode = MODE_READ;
        return super.signal('open');
    }
    
    void createTransmittal(){
        entity.tolgu?.type = entity.tolgu?.lgutype
        entity.putAll(svc.create(entity));
        mode = MODE_READ;
    }
    
    void edit(){
        mode = MODE_EDIT;
    }
    
    void cancelEdit(){
        entity = svc.open(entity);
        entity.items = svc.getItems(entity);
        mode = MODE_READ;
    }
    
    void save(){
        entity.putAll(svc.update(entity));
        mode = MODE_READ;
    }
    
    void delete(){
        svc.delete(entity);
    }
    
    void submit(){
        checkItems();
        entity.putAll(svc.submit(entity));
    }
    
    void submitForApproval(){
        checkItems();
        entity.putAll(svc.submitForApproval(entity));
    }
    
    void checkItems(){
        if (!entity.items)
            throw new Exception('Unable to submit transmittal.\nAt least one item is required.');
    }
    
    void approve(){
        entity.putAll(svc.approve(entity));
    }
    
    List getTransmittalTypes(){
        return svc.getTransmittalTypes();
    }
    
   List getLgus(){
        def lgus = [];
        if (OsirisContext.env.ORGCLASS.equalsIgnoreCase('PROVINCE')){
            lgus = lguSvc.lookupMunicipalities([:]);
            def muni = lgus.find{it.objid == OsirisContext.env.ORGID};
            if (muni)
                lgus.remove(muni);
        }
        else{
            lgus = lguSvc.lookupProvinces([:]);
        }
        
        return lgus;
        
    }
    
    boolean getShowExport(){
        if (entity.state == 'SUBMITTED' && entity.lgu.objid == OsirisContext.env.ORGID)
            return true;
        if (entity.state == 'FORAPPROVAL' && entity.lgu.objid == OsirisContext.env.ORGID)
            return true;
        if (entity.state == 'APPROVED' && entity.tolgu.objid == OsirisContext.env.ORGID)
            return true;
        return false;
    }
    
    boolean getShowForApprovalActions(){
        if (entity.state == 'FORAPPROVAL' && entity.tolgu.objid == OsirisContext.env.ORGID)
            return true;
        return false;
    }    
        
    
    /*============================================
    ** ITEMS SUPPORT
    **=============================================*/
    def data;
    def selectedItem;
    
    def abstract getLookupHandler();
    
    void validateItem(item){
        if (!item.objid)
            item.objid = 'TI' + new java.rmi.server.UID();
        if (!item.parentid)
            item.parentid = entity.objid; 
        if (!item.filetype)
            item.filetype = getFileType();

        RPTUtil.required('File Type', item.filetype);
        RPTUtil.required('State', item.state);
        RPTUtil.required('Ref ID', item.refid);
        RPTUtil.required('Ref No.', item.refno);
    }
    
    def listHandler = [
        createItem : { return [
             objid    : 'TI' + new java.rmi.server.UID(),
             parentid : entity.objid,
             filetype : getFileType(),
             isnew    : true,
        ]},
    
        onAddItem   : {item ->
            svc.saveItem(item);
            entity.items << item;
        }, 
        
        onRemoveItem : {item ->
            if (MsgBox.confirm('Delete selected item?')){
                svc.deleteItem(item);
                entity.items.remove(item)
                return true;
            }
            return false;
        },
        
        validate : {li ->
            def item = li.item;
            if (!item.isnew){
                svc.saveItem(item);
            }
        },
        
        fetchList : { return entity.items },
    ] as EditorListModel;
    
    @Close()
    void onClose(){
        if (oncomplete) oncomplete();
    }
}