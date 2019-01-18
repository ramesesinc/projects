package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.common.*;

class ResectionInfoMainController
{
    @Binding
    def binding;
    
    @Caller
    def caller;
    
    @Service('ResectionService')
    def svc;
    
    @Service('FAASService')
    def faasSvc;
    
    def entity;
    String entityName = 'resection:info:main'
    
    String title = 'Resection'
    
    def selectedItem;
    def mode;
    
    void init() {
        mode = 'read';
    }
    
    void edit() {
        mode = 'edit';
    }
    
    void save() {
        mode = 'read';
    }
    
    def viewFaas() {
        def faas = faasSvc.openFaas(selectedItem.newfaas)
        def inv = Inv.lookupOpener('faas:info', [entity: faas]);
        inv.target = 'popup';
        return inv;
    }
    
    def deleteFaas() {
        if (MsgBox.confirm('Delete resectioned FAAS?')) {
            selectedItem.putAll(svc.deleteFaas(selectedItem));
            entity.putAll(svc.open([objid: entity.objid]));
            binding.refresh();
            listHandler.reload();
        }
    }
    
    
    
    def listHandler = [
        fetchList : { entity.items },
        onColumnUpdate: {item, colname ->
            item.pintype = entity.pintype;
            item.memoranda = entity.memoranda;
            if ('newfaas.parcel' == colname) {
                item.putAll(svc.validateNewParcel(item));
                buildPin(item);
            } else if ('newfaas.suffix' == colname) {
                svc.validateNewParcel(item);
                svc.validateNewSuffix(item);
                buildPin(item);
            }
        },
        validate: {li ->
            def item = li.item;
            if (!item.newfaas.objid) {
                item.putAll(svc.createNewFaas(item));
            }
            if ('land' == item.faas.rputype) {
                updateImprovementInfo(item);
            }
            svc.updateItem(item);
            listHandler.reload();
        }
    ] as EditorListModel;
    
    void updateImprovementInfo(item) {
        def improvements = entity.items.findAll{it.faas.rputype != 'land' && it.faas.pin == item.faas.pin};
        improvements.each{
            it.newfaas.rpid = item.newfaas.rpid;
            it.newfaas.section = item.newfaas.section;
            it.newfaas.parcel = item.newfaas.parcel;
            svc.updateItem(it);
        }
    }
    
    void buildPin(item) {
        def pins = [];
        pins << entity.barangay.pin;
        if ('new' == entity.pintype) {
            pins << (item.newfaas.section + '').padLeft(3, '0');
            pins << (item.newfaas.parcel + '').padLeft(2, '0');
        } else {
            pins << (item.newfaas.section + '').padLeft(2, '0');
            pins << (item.newfaas.parcel + '').padLeft(3, '0');
        }
        if (item.newfaas.suffix && item.newfaas.suffix != 0) {
            pins << item.newfaas.suffix;
        }
        item.newfaas.fullpin = pins.join('-');
    }
    
    boolean getShowActions(){
        if (entity.originlgu.objid != OsirisContext.env.ORGID) return false;
        if (entity.taskstate && entity.taskstate.matches('assign.*')) return false;
        if (entity.taskstate && !entity.taskstate.matches('.*taxmapper.*')) return false;
        if (entity.state.matches('APPROVED')) return false;
        return true;
    }
        
    
    
    
    
    def popupSupportInfo(def inv) {
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:entity] ).findAll{
            def vw = it.properties.visibleWhen;
            return  ((!vw)  ||  ExpressionResolver.getInstance().evalBoolean( vw, [entity:entity, orgid:OsirisContext.env.ORGID] ));
        }
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }
    
    def getShowSupport(){
        if (entity.state == 'DRAFT') 
            return false;
        return true;
    }
    
}