package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;

public class MachForexInfoModel implements SubPage
{
    @Binding
    def binding 
    
    //MachRYSettingService
    def service;
    
    def entity;
    def mode = 'read';
    
        
    void init(){
        forexes = service.getForexes(entity);
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
    
    /*---------------------------------------------------------------------
    *
    * Forex Support
    *
    ---------------------------------------------------------------------*/
    def selectedForex 
    def forexes = []
    
    def forexListHandler  = [
        createItem : { return createForex() },
        getRows    : { return (forexes.size() <= 25 ? 25 : forexes.size() + 1)},
                
        getColumns : { return [
            new Column(name:'year', caption:'Year*', type:'integer', format:'0000', editable:true ),
            new Column(name:'forex', caption:'Rate*', type:'decimal', editable:true, typeHandler:new DecimalColumnHandler(scale:4, format:'#,##0.0000')  ),
        ]},
    
        onColumnUpdate : {item, colname ->
            if (item.year == null && forexes.size() > 0){
                item.year = forexes.last().year + 1;
            }
        },
        
        validate : { li -> 
            def forex = li.item 
            def duplicate = false;
            if (forex.isnew){
                duplicate = forexes.find{it.year == forex.year} != null
            }
            else {
                duplicate = forexes.findAll{it.year == forex.year}.size() > 1
            }
            if (duplicate) throw new Exception('Duplicate year is not allowed.')
            service.saveForex( forex )
        },
                
        onAddItem  : { item -> forexes.add( item ) },
                
        onRemoveItem : { forex -> 
            if( MsgBox.confirm('Delete selected forex?')) {
                service.deleteForex( forex ) 
                forexes.remove( forex )
                return true
            }
            return false;
        },
                
        fetchList    : { return forexes },
    ] as EditorListModel

    Map createForex() {
        return [
            objid               : 'FO' + new java.rmi.server.UID(),
            machrysettingid     : entity.objid,
            isnew               : true,
        ]
    }
    
    def isLastItem( list, item ) {
        def index = list.indexOf( item )
        if( index < 0 ) 
            index = list.size()
        else
            index += 1 
        return list.size() == index 
    }
            
}