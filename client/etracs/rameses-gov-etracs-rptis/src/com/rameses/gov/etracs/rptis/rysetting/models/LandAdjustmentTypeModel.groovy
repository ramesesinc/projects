package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import java.rmi.server.UID

public class LandAdjustmentTypeModel 
{
    @Binding 
    def binding
    
    @Service('LandRYSettingService')
    def svc;
    
    def addHandler
    def updateHandler
    
    def mode 
    def entity
    def adjustments
    def selectedItem
        
    void create() {
        mode = 'create'
        entity = createEntity()
    }
    
    void open() {
        println 'mode -> ' + mode 
    }
    
    def save() {
        invokeAddHandler()
        return '_close' 
    }
    
    void saveAndCreate() {
        invokeAddHandler()
        entity = createEntity()
        binding.refresh()
        classificationListHandler.load()
        binding.focus('entity.adjustmentcode')
    }
    
    def update() {
        checkDuplicates()
        if( updateHandler ) updateHandler( entity )
        return '_close' 
    }
    
    void invokeAddHandler() {
        checkDuplicates()
        if( addHandler ) addHandler( entity )
    }
    
    void checkDuplicates(){
        checkDuplicate( 'Code', 'code')
        checkDuplicate( 'Name', 'name')
    }
    
    void checkDuplicate(caption, fieldname) {
        def data = adjustments.find{ it.objid != entity.objid && it[fieldname] == entity[fieldname] }
        if( data ) throw new Exception( 'Duplicate ' + caption + ' is already defined.')
    }
    
    def createEntity() {
        return [ 
            objid               : 'LA' + new UID(),
            classifications     : []
        ]
    }
    
    def classificationListHandler = [
        getRows    : { return (entity.classifications.size() <= 20 ? 20 : entity.classifications.size()+1) },
        getColumns : {
            return [
                new Column(name:"classification", caption:"Code", minWidth:50, editable:true, type:"lookup", handler:"propertyclassification:lookup", expression: '#{item.classification.code}'),
                new Column(name:"classification.name", caption:"Name", editable:false)
            ];
        },
        fetchList : { 
            return entity.classifications; 
        },
        createItem : { return [:]; },
        validate     : { li -> 
            def item = li.item 
            if( ! item.classification ) throw new Exception('Code is required.')

            //check duplicate item 
            def data = entity.classifications.find{ it.classification.objid == item.classification.objid}
            if( data ) throw new Exception('Duplicate item is not allowed.')    
        },
        
        onAddItem : { item ->entity.classifications.add( item ); },
        onRemoveItem : { item -> 
            if( mode == "view" ) return false;
            if( MsgBox.confirm("Remove selected item?") ) {
                entity.classifications.remove( item );
                return true;
            }
            return false;
        },
    ] as EditorListModel;
    
    
    def _expr;
    
    def editExpression() {
        _expr = entity.expr;
        def model = [
            getValue: {
                return _expr;
            },
            setValue: { o->
                _expr = o;
            },
            getVariables: { type->
                def vars = svc.getVariableList();
                return vars.collect{
                    [caption: it.name, title:it.name,  signature: it.name, description : "("+it.paramtype +")" ]
                };
            }
        ] as ExpressionModel;
        def handle = { o-> 
            entity.expr = _expr;
            binding.refresh("entity.expr") 
        };
        return InvokerUtil.lookupOpener("expression:editor", [model:model, updateHandler: handle] );
    }
       
}

