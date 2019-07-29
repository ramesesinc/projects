package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;

public class PlantTreeUnitValueInfoModel implements SubPage
{
    @Binding
    def binding 
    
    //PlantTreeRYSettingService
    def service;
    
    def entity;
    def mode = 'read';
    
        
    void init(){
        plantTrees = service.getPlantTrees([:])
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
    /*---------------------------------------------------------------------
    *
    * PlantTreeUnit Support
    *
    ---------------------------------------------------------------------*/
    def searchtext;
    
    void search(){
        def params = [searchtext:searchtext]
        plantTrees = service.getPlantTrees(params)
        planttreeListHandler.reload();
    }
    
    
    
    def plantTrees = [] 
    def selectedPlantTree
    def selectedValue
    def unitValueTitle 
    
    void setSelectedPlantTree( selectedPlantTree ) {
        this.selectedPlantTree = selectedPlantTree
        unitValues.clear()
        unitValueTitle = 'Please select a Plant or Tree.'
        if( selectedPlantTree ) {
            unitValues = service.getUnitValues( entity, selectedPlantTree)
            unitValueTitle = selectedPlantTree.name 
        }
        unitvalueListHandler.load()
    }
    
    
    def planttreeListHandler  = [
        getRows    : { return plantTrees.size() <= 25 ? 25 : plantTrees.size() + 1 },
        getColumns : { return [
            new Column( name:'code', caption:'Code', maxWidth:80 ),
            new Column( name:'name', caption:'Plant/Tree' ),
        ]},
        fetchList    : { return plantTrees },
    ] as BasicListModel
    
    
    /*---------------------------------------------------------------------
    *
    * PlantTreeUnitValue Support
    *
    ---------------------------------------------------------------------*/
    List unitValues = []
    
    def unitvalueListHandler = [
        createItem : { return createUnitValue() },
        getRows    : { return (unitValues.size() <= 25 ? 25 : unitValues.size() + 1 ) },
        getColumns : { return [
            new Column(name:'code', caption:'Code*', maxWidth:60, editable:true ),
            new Column(name:'name', caption:'Name*', editable:true ),
            new Column(name:'unitvalue', caption:'Unit Value*', type:'decimal', editable:true ),
        ]},
    
        onColumnUpdate : { item, colName -> 
            if (colName=='code'){
                if (item.code && item.code.matches('.*[0-9]')){
                    def num = item.code[-1];
                    if (num == '1') item.name = '1ST CLASS'
                    else if (num == '2') item.name = '2ND CLASS'
                    else if (num == '3') item.name = '3RD CLASS'
                    else item.name = num + 'TH CLASS'
                }
            }
        },
                
        validate : { li -> 
            def uv = li.item 
            RPTUtil.required(uv.code, 'Code')
            RPTUtil.required(uv.name, 'Name')
            RPTUtil.checkDuplicate( unitValues, 'Code', 'code', uv.objid, uv.code )
            RPTUtil.checkDuplicate( unitValues, 'Name', 'name', uv.objid, uv.name )
            uv.putAll(service.saveUnitValue( uv ))
        },
                
        onRemoveItem   : { uv ->
            if( MsgBox.confirm('Remove item?' ) ) {
                service.deleteUnitValue( uv ) 
                unitValues.remove( uv )
                return true;
            }
            return false;
        },
                
        onAddItem      : { item -> unitValues.add( item ) },
        fetchList      : { return unitValues },
    ] as EditorListModel
    
    
    Map createUnitValue() {
        def idx = unitValues.size() + 1
        def code = selectedPlantTree.code + '' + idx
        def name = RPTUtil.formalizeNumber(idx) + ' CLASS'
        return [
            objid                   : RPTUtil.generateId('BT'),
            planttreerysettingid    : entity.objid, 
            planttree_objid         : selectedPlantTree.objid,
            planttree               : selectedPlantTree,
            code                    : code,
            name                    : name,
            unitvalue               : 0.0,
        ]
    }    
    
}