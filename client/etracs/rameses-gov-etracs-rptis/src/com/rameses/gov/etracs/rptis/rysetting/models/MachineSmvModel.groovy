package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;

public class MachineSmvModel extends CrudFormModel 
{
    @Binding 
    def binding
    
    @Service('RPTParameterService')
    def paramSvc 

    boolean isShowConfirm() { return false; }

    void afterSave() {
        caller?.reload();
    }

    void beforeSave(mode) {
        if (mode == 'create') {
            entity.parent = [objid: caller?.masterEntity?.objid];
        }
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
