package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;


public class BldgTypeInfoModel implements SubPage
{
    @Binding
    def binding 
    
    //BldgRYSettingService
    def service;
    
    def entity;
    def mode = 'read';
    
        
    void init(){
        bldgTypes = service.getBldgTypes(entity);
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
    
    /*---------------------------------------------------------------------
    *
    * BldgType Support
    *
    ---------------------------------------------------------------------*/
    @PropertyChangeListener
    def listener = [
        'selectedBldgType.residualrate' : {
            service.saveBldgType(selectedBldgType);
        },
        'selectedBldgType.usecdu' : {
            service.saveBldgType(selectedBldgType);
            initDepreciationHandler()
        },
    ]
    
    def bldgTypes = [] 
    def selectedBldgType
    
    void setSelectedBldgType( selectedBldgType ) {
        this.selectedBldgType = selectedBldgType
        baseValueType = selectedBldgType?.basevaluetype
        initBaseValueHandler()
        initDepreciationHandler()
        binding.refresh('baseValueType') 
        
        depreciations = service.getDepreciations(selectedBldgType)
        depreciationListHandler.load();
        binding.requestFocus('selectedBldgType');
    }
    
    def bldgTypeListHandler = [
        createItem : { return createBldgType() },
        getRows    : { return (bldgTypes.size() <= 25 ? 25 : bldgTypes.size() + 1)},
        getColumns : { return [
            new Column(name:'code', caption:'Code*', maxWidth:60, editable:true ),
            new Column(name:'name', caption:'Building Type*', editable:true ),
        ]},
                
        validate       : { li -> 
            def bldgType = li.item
            RPTUtil.required( bldgType.code, 'Code' )
            RPTUtil.required( bldgType.name, 'Bldg Type' )
            checkDuplicateBldgType( bldgType )
            if ( !bldgType.objid ) bldgType.objid = RPTUtil.generateId('BT')
            service.saveBldgType( bldgType )
        },
                
        onRemoveItem   : { bldgType -> 
            if( MsgBox.confirm('Remove item?') ) {
                service.deleteBldgType( bldgType )
                bldgTypes.remove( bldgType )
                return true;
            }
            return false;
        },
                
        onAddItem      : { item -> bldgTypes.add( item ) },
        fetchList      : { return bldgTypes },
    ] as EditorListModel
    
    
    void checkDuplicateBldgType( bldgType ) {
        def item = bldgTypes.find{ it.objid != bldgType.objid && it.code == bldgType.code  }
        if( item ) throw new Exception('Duplicate code is not allowed.')    
        item = bldgTypes.find{ it.objid != bldgType.objid && it.name == bldgType.name }
        if( item ) throw new Exception('Duplicate name is not allowed.')    
    }
    

    /*---------------------------------------------------------------------
    *
    * Depreciation Support
    *
    ---------------------------------------------------------------------*/
    def depreciationHandler
    
    void initDepreciationHandler() {
        depreciationHandler = Inv.lookupOpener('bldgtype:depreciation',[bldgType:selectedBldgType, mode:mode, service:service])
    }
    
    
    /*---------------------------------------------------------------------
    *
    * BldgKind Support
    *
    ---------------------------------------------------------------------*/
    def baseValueType
    def baseValueHandler
    def selectedBaseValue
    def searchtext;
   
    void search(){
        if (baseValueHandler){
            def params = [bldgtypeid:selectedBldgType.objid, searchtext:searchtext]
            baseValueHandler.handle.search(params);
        }
    }
    
    void setBaseValueType( baseValueType ) {
        this.baseValueType = baseValueType
        if( selectedBldgType && selectedBldgType.objid ) {
            selectedBldgType.basevaluetype = baseValueType
            service.saveBldgType( selectedBldgType )
            initBaseValueHandler()
        }
    }
    
    void initBaseValueHandler() {
        baseValueHandler = null
        if( baseValueType ) {
            def openerType = 'basevaluetype:' + baseValueType
            baseValueHandler = InvokerUtil.lookupOpener(openerType, [bldgType:selectedBldgType, mode:mode, service:service] )
        }
    }
    
    
    List getBaseValueTypes() {
        return ['fix','range','gap']
    }
    
     void validateRate( rate ) {
        if( rate < 0.0 ) throw new Exception('Rate must be greater than or equal to zero.')
        if( rate > 100) throw new Exception('Rate must not exceed 100.00')
    }       
    
    Map createBldgType() {
        return [
            code            : null,
            name            : null,
            bldgrysettingid : entity.objid,
            basevaluetype   : 'fix', 
            residualrate    : 0.0,
            multistoreyadjustments : [],
            depreciations : [],
        ]
    }
    
    Map createDepreciation(){
        return [
            objid           : RPTUtil.generateId('BD'),
            bldgtypeid      : selectedBldgType?.objid,
            bldgrysettingid : entity.objid,
            isnew           : true,
        ]
    }
 
        
    def isFirstItem( list ) {
        return list.size() == 0
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