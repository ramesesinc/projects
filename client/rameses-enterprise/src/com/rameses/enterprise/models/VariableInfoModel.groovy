package com.rameses.enterprise.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
        
public class VariableInfoModel extends CrudFormModel {
        
    def arrayvalue;
    
    @PropertyChangeListener
    def listener = [
        "entity.datatype": {o->
            entity.arrayvalues = []
            binding.refresh('arrayvalue')
        },
        "entity.name": {o->
            entity.objid = o;
        },
    ]

    void afterCreate() {
        entity.system = 0;
        entity.arrayvalues = [];
    }

    void addArrayValue() {
        def value = MsgBox.prompt("Enter value: ")
        if( !value || value == 'null' ) return
        if( !entity.arrayvalues.find{ it == value } ) {
            entity.arrayvalues.add(value.toUpperCase())
            binding.refresh('arrayvalue')
        }
    }

    void removeArrayValue() {
        if( MsgBox.confirm("Remove selected array value?") ) {
            entity.arrayvalues.remove(arrayvalue)
            binding.refresh('arrayvalue')
        }
    }

}