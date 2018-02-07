package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;


public class BldgAdditionalItemInfoModel implements SubPage
{
    @Binding
    def binding 
    
    //BldgRYSettingService
    def service;
    
    def entity;
    def mode = 'read';
    
        
    void init(){
        additionalItems = service.getAdditionalItems(entity);
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
        
    /*---------------------------------------------------------------------
    *
    * BldgAdditionalItem Support
    *
    ---------------------------------------------------------------------*/
    def additionalItems = []
    def selectedAdditionalItem
    def searchtext;
    
    void searchAdditionalItems(){
        additionalItems = service.searchAdditionalItems([bldgrysettingid:entity.objid, searchtext:searchtext])
        additionalItemListHandler?.reload();
    }
    
    
    def additionalItemListHandler = [
        getRows    : { return (additionalItems.size() <= 20 ? 20 : additionalItems.size() + 1) },
        getColumns : { return [
            new Column(name:'code', caption:'Code', maxWidth:60),
            new Column(name:'name', caption:'Name'),
            new Column(name:'unit', caption:'Unit', maxWidth:100 ),
            new Column(name:'expr', caption:'Expression'),
            new Column(name:'type', caption:'Type'),
        ]},
        onRemoveItem   : { item -> doRemoveAdditionalItem( item ) },
        fetchList      : { return additionalItems },
        onOpenItem     : {item, colname -> openAdditionalItem()},
    ] as BasicListModel 
    
    
    
    def addAdditionalItemHandler = { item -> 
        item.bldgrysettingid = entity.objid
        service.saveAdditionalItem( item )
        additionalItems.add( item )
        additionalItemListHandler.load()
    }
    

    def updateAdditionalItemHandler = { item -> 
        service.saveAdditionalItem( item );
        additionalItems.set( additionalItems.indexOf( selectedAdditionalItem), item )
        binding.refresh('selectedAdditionalItem')
    }
    

    def createAdditionalItem() {
        return InvokerUtil.lookupOpener('bldgadditionalitem:create', [addHandler:addAdditionalItemHandler] )
    }
    
    def openAdditionalItem() {
        if( selectedAdditionalItem && mode != 'read' ){
            return InvokerUtil.lookupOpener('bldgadditionalitem:open', [entity:selectedAdditionalItem, updateHandler:updateAdditionalItemHandler] )
        }
    }
    
    void removeAdditionalItem() {
        if( selectedAdditionalItem ) {
            doRemoveAdditionalItem( selectedAdditionalItem )
        }
    }
    
    
    void doRemoveAdditionalItem( item ) {
        if( MsgBox.confirm('Remove item?') ) {
            service.deleteAdditionalItem( selectedAdditionalItem )
            additionalItems.remove( item )
        }
    }
    
       
}