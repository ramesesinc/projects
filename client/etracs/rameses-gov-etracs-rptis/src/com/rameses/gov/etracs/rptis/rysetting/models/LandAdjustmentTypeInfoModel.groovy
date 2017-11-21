package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;

public class LandAdjustmentTypeInfoModel implements SubPage
{
    @Binding
    def binding 
    
    def service;
    
    def entity;
    def mode = 'read';
    
    def items = []
    def selectedItem
    
        
    void init(){
        items = service.getAdjustmentTypes(entity);
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
    /*==================================================================
    * 
    *  LANDADJUSTMENTTYPE  SUPPORT
    *
    ==================================================================*/
    def listHandler = [
        getRows    : { return items.size()+1},
        getColumns : { return [
            new Column(name:'code', caption:'Code', maxWidth:60),
            new Column(name:'name', caption:'Name'),
            new Column(name:'appliedto', caption:'Applied To', maxWidth:150 ),
            new Column(name:'expr', caption:'Formula', width:200),
            new Column(name:'idx', caption:'Print Order', width:70, type:'integer'),
        ]},
        onRemoveItem   : { item -> return removeAdjustmentType( item ) },
        onOpenItem     : { item, colname -> openAdjustmentType() },
        fetchList      : { return items },
    ] as BasicListModel 

    
    def addLandAdjustmentTypeHandler = { item -> 
        item.appliedto = item.classifications.classification.code.join(',')
        item.landrysettingid = entity.objid
        service.saveAdjustmentType( item ) 
        if( ! items ) {
            items = []
        }
        items.add( item )
        listHandler.reload()
    }
    

    def updateLandAdjustmentTypeHandler = { item -> 
        item.appliedto = item.classifications.classification.code.join(',')
        service.saveAdjustmentType( item ) 
        items.set( items.indexOf( selectedItem ), item )
        binding.refresh('selectedItem')
    }

    
    def createAdjustmentType() {
        return InvokerUtil.lookupOpener('landadjustmenttype:create', [addHandler:addLandAdjustmentTypeHandler, adjustments:items] )
    }

    
    def openAdjustmentType() {
        if( selectedItem && mode != 'read') {
            def adjustment = service.openAdjustmentType(selectedItem)
            return InvokerUtil.lookupOpener('landadjustmenttype:open', [entity:adjustment, updateHandler:updateLandAdjustmentTypeHandler, adjustments:items, mode:mode] )
        }
    }
    

    void removeAdjustmentType() {
        if( selectedItem ) {
            removeLandAdjustmentType( selectedItem )
        }
    }
    
    
    def removeLandAdjustmentType( item ) {
        if( MsgBox.confirm('Remove item?') ) {
            service.deleteAdjustmentType( selectedItem )
            items.remove( item )
            return true 
        }
        return false 
    }
    
}