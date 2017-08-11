package com.rameses.enterprise.accounting.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class StandardReportModel { 

    def criteriaList = [];
    
    def criteriaHandler = [
        
        getFieldList: { 
            def list = [
                [key:'NEW', value:'NEW'],
                [key:'RENEW', value:'RENEW'],
                [key:'RETIRE', value:'RETIRE'],
            ]
            return [
                [name: 'firstname', caption: 'First Name'],
                [name: 'salary', caption:'Salary', type: 'decimal' ],
                [name:'birthdate', caption:'Birth Date', type:'date'],
                [name:'year', caption:'Year', type:'integer'],
                [name:'apptype', caption:'App Type', type:'list', list: list],
                [name:'customer', caption:'Customer', type:'list', schemaname:'entity', lookupkey:"objid", lookupvalue:"name"],
            ]
        },
        
        add: { o->
            criteriaList << o; 
        },
        
        remove: {
            criteriaList.remove(o);
        },
        
        clear: { criteriaList.clear(); }
        
    ]
    
    void show() {
        MsgBox.alert( criteriaList );
    }
    
} 

