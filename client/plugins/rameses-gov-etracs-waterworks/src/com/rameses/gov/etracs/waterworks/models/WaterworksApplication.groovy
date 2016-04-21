package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksApplication extends WorkflowTaskModel {
    
    def tabList;
    
    void afterOpen() {
        tabList = [];
        def m = [schemaname:getSchemaName(), refid:entity.objid];
        tabList.add( Inv.lookupOpener( "waterworks_application_fee:list", [entity:entity] ));
        tabList.add( Inv.lookupOpener( "requirements:list", m ));
    }
    
    void refresh() {
        tabList.each { op->
            try {
                op.code.refresh();    
            }catch(e){println e.message;}
        }
        binding.refresh();
    }
    
    public void afterSignal() {
        if(task.acctno) {
            MsgBox.alert("Account created " + task.acctno);
        }
    }
    
    
    
}