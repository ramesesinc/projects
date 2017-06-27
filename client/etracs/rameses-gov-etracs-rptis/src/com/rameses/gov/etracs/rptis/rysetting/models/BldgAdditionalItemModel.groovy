package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.UID

public class BldgAdditionalItemModel
{
    @Binding 
    def binding
    
    @Service('RPTParameterService')
    def paramSvc 
    
    
    @Service('RPUService')
    def rpuSvc
    
    def addHandler
    def updateHandler
    
    def mode 
    def entity
    
    def types = LOV.RPT_BLDG_ADJUSTMENT_TYPES*.key
        
    void create() {
        mode = 'create'
        entity = createEntity()
    }
    
    void open() {
        mode = 'open'
    }
    
    def save() {
        invokeAddHandler()
        return '_close' 
    }
    
    void saveAndCreate() {
        invokeAddHandler()
        entity = createEntity()
        binding.refresh()
        binding.focus('entity.code')
    }
    
    def update() {
        if( updateHandler ) updateHandler( entity )
        return '_close' 
    }
    
    void invokeAddHandler() {
        if( addHandler ) addHandler( entity )
    }
    
    def createEntity() {
        return [ objid : 'BI' + new UID(), addareatobldgtotalarea:0]
    }
    

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
                def vars = paramSvc.getVariableList();
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
