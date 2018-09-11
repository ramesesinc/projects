package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class BusinessVariableModel extends CrudFormModel {

    String prefixId = 'BV'
    def arrayvalue
    def datatypes = LOV.VAR_DATA_TYPES
    def vartype = 'USER-DEFINED';

    @PropertyChangeListener
    def listener = [
        "entity.datatype": {o->
            entity.arrayvalues = []
            binding.refresh('arrayvalue')
        }
    ]
    
    void afterCreate() {
        vartype = (entity.system==1) ? 'SYSTEM' : 'USER-DEFINED';
        entity.system = 0;
        entity.arrayvalues = [];
        entity.state = 'DRAFT';
        entity.sortorder = 1;
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