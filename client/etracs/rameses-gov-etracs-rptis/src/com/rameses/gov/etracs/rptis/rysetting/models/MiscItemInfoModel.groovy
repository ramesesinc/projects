package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.*;

public class MiscItemInfoModel implements SubPage
{
    @Binding
    def binding 
    
    @Service('RPTParameterService')
    def paramSvc
    
    //MiscRYSettingService
    def service;
    
    def entity;
    def mode = 'read';
    
        
    void init(){
        miscItemValues  = service.getMiscItemValues(entity)
    }
    
    public void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
    /*---------------------------------------------------------------------
    *
    * MiscItemValue Support
    *
    ---------------------------------------------------------------------*/
    def selectedMiscItem 
    def miscItemValues = []
    def searchtext;
    
    void search(){
        def params = [miscrysettingid:entity.objid, searchtext:searchtext]
        miscItemValues = service.searchMiscItemValues(params);
        miscItemListHandler.reload();
    }
    
    
    def _expr;
    
    def handle = { o-> 
        selectedMiscItem.expr = _expr;
        miscItemListHandler.refreshEditedCell()
    };
    
    def getLookupEditor() {
        _expr = selectedMiscItem.expr;
        def model = [
            getValue: {
                return _expr;
            },
            setValue: { o->
                _expr = o;
            },
            getVariables: { type->
                def vars = paramSvc.getVariableList();
                return vars.collect{
                    [caption: it.name, title:it.name,  signature: it.name, description : "("+it.paramtype +")" ]
                };
            }
        ] as ExpressionModel;
        
        return InvokerUtil.lookupOpener("expression:editor", [model:model, updateHandler:handle] );
    }


    
    def getLookupMiscItem(){
        return InvokerUtil.lookupOpener('miscitem:lookup',[
            onselect: { item ->
                selectedMiscItem.miscitem = item
            },
            onempty: {
                selectedMiscItem.miscitem = null
            }
        ])
    }
    
    def miscItemListHandler = [
        getRows    : { return (miscItemValues.size() <= 25 ? 25 : miscItemValues.size() + 1) },
            
        createItem : { return [
            objid : RPTUtil.generateId('MI'),
            miscrysettingid : entity.objid,
            isnew : true,
        ]},
            
        getColumns : { return [
            new Column(name:'miscitem', caption:'Code', type:'lookup', handler:'miscitem:lookup', editable:true, expression:'#{item.miscitem.code}',  maxWidth:80, required:true),
            new Column(name:'miscitem.name', caption:'Name'),
            new Column(name:"expr", editable:true, caption:"Computation Expression", typeHandler: new OpenerColumnHandler( handler: "lookupEditor" ), required:true )
        ]},
                
        validate : { li -> 
            def miv = li.item
            checkDuplicate(miv)
            service.saveMiscItemValue( miv )
        },
        
        onRemoveItem :{item -> 
            if (MsgBox.confirm('Delete item?')){
                service.deleteMiscItemValue(item);
                miscItemValues.remove(item);
                return true;
            }
            return false;
        },
                
                                
        onAddItem : { item ->
            miscItemValues.add(item);
            item.isnew = false;
        },
                
        fetchList    : { return miscItemValues },
    ] as EditorListModel
                
    void checkDuplicate(miv){
            def data = miscItemValues.find{it.miscitem.objid == miv.miscitem.objid && it.objid != miv.objid }
            if (data)
                throw new Exception("Duplicate item is not allowed.")
    }
    
}