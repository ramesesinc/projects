package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*


class SubdivisionMotherLandModel
{
    @Caller
    def caller;
    
    
    @Binding
    def binding;
    
    @Service('FAASService')
    def faasSvc;
    
    @Service('SubdivisionService')
    def svc; 
    
    def entity; //subdivision 
    def motherlands;
    def selectedItem;
    def mode; 
    
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
    
    String title = 'Mother Land(s) to Subdivide';
    
    void init(){
        motherlands = svc.getMotherLands(entity);
        mode = MODE_READ;
    }
    
    void edit(){
        mode = MODE_EDIT;
    }
    
    void update(){
        mode = MODE_READ;
    }
    
       
    def listHandler = [
        createItem   : { return [
            objid           : 'SML' + new java.rmi.server.UID(),
            subdivisionid   : entity.objid
        ]},
        
        getRows      : { motherlands.size() + 1},
        
        fetchList    : { motherlands },
        
        validate : {li ->
            def item = li.item 
            if (item.faas.state != 'CURRENT' )
                throw new Exception('FAAS is invalid. Only current record is allowed.');
            if (item.faas.rputype != 'land')
                throw new Exception('FAAS is invalid. Only land property is allowed.');

            def dup = motherlands.find{it.objid == item.faas.objid}
            if (dup) throw new Exception('Duplicate FAAS is not allowed.')

            item.landfaasid = item.faas.objid;
            item.rpuid = item.faas.rpuid;
            item.rpid = item.faas.realpropertyid;
            item.realpropertyid = item.faas.realpropertyid;
            item.lguid = item.faas.lguid;
            item.lgutype = item.faas.lgutype;
            item.tdno = item.faas.tdno;
            item.owner = item.faas.owner;
            item.fullpin = item.faas.fullpin;
            item.totalmv = item.faas.totalmv; 
            item.totalav = item.faas.totalav; 
            item.totalareaha = item.faas.totalareaha; 
            item.totalareasqm = item.faas.totalareasqm; 
            item.barangayid = item.faas.barangayid;
        },
        
        onRemoveItem : {item ->
            if (MsgBox.confirm('Remove selected item?')){
                svc.deleteMotherLand(item);
                motherlands.remove(item);
                binding.refresh('count');
                return true;
            }
            return false;
        },
        
        onAddItem : {item ->
            svc.addMotherLand(item);
            motherlands << item;
        },

        openItem : {item, colName ->
            return viewFaas();    
        }
    ] as EditorListModel;
    
    def viewFaas() {
        def faas = [objid: selectedItem.landfaasid, tdno: selectedItem.tdno];
        return Inv.lookupOpener('faas:report', [entity: faas]);
    }
    
    
    boolean getShowActions(){
        if (entity.originlguid != OsirisContext.env.ORGID) return false;
        if (entity.taskstate && entity.taskstate.matches('assign.*')) return false;
        if (entity.taskstate && !entity.taskstate.matches('.*taxmapper.*')) return false;
        if (entity.state.matches('APPROVED')) return false;
        return true;
    }
    
    
    void addMessage(msg){
        caller.addMessage(msg);
    }
    
    void clearMessages(msg){
        caller.clearMessages(msg);
    }
    
}
       